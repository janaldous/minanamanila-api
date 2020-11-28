package com.janaldous.minanamanila.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Sort;

import com.janaldous.minanamanila.data.AddressRepository;
import com.janaldous.minanamanila.data.DeliveryDate;
import com.janaldous.minanamanila.data.DeliveryType;
import com.janaldous.minanamanila.data.OrderDetail;
import com.janaldous.minanamanila.data.OrderItem;
import com.janaldous.minanamanila.data.OrderRepository;
import com.janaldous.minanamanila.data.OrderStatus;
import com.janaldous.minanamanila.data.OrderTracking;
import com.janaldous.minanamanila.data.OrderTrackingRepository;
import com.janaldous.minanamanila.data.PaymentType;
import com.janaldous.minanamanila.data.Product;
import com.janaldous.minanamanila.data.ProductRepository;
import com.janaldous.minanamanila.data.User;
import com.janaldous.minanamanila.data.UserRepository;
import com.janaldous.minanamanila.domain.mapper.OrderItemMapper;
import com.janaldous.minanamanila.testutil.ProductDtoMockFactory;
import com.janaldous.minanamanila.testutil.TestUtils;
import com.janaldous.minanamanila.webfacade.dto.AddressDto;
import com.janaldous.minanamanila.webfacade.dto.OrderConfirmation;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;
import com.janaldous.minanamanila.webfacade.dto.OrderUpdateDto;
import com.janaldous.minanamanila.webfacade.dto.UserDto;

class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AddressRepository addressRepository;

	@Mock
	private OrderTrackingRepository orderTrackingRepository;
	
	@Mock
	private DeliveryDateService deliveryDateService;
	
	@Spy
	private OrderItemMapper orderItemMapper = new OrderItemMapper();

	@InjectMocks
	private OrderService orderService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testCreateOrder() {
		assertEquals(0, orderRepository.count());
		
		OrderDto orderDto = new OrderDto();
		AddressDto address = new AddressDto();
		address.setLine1("Main Street");
		orderDto.setAddress(address);
		orderDto.setDeliveryType(DeliveryType.DELIVER);
		orderDto.setPaymentType(PaymentType.CASH);
		orderDto.setProducts(ProductDtoMockFactory.getMockProducts());
		orderDto.setDeliveryDateId(1l);
		UserDto user = new UserDto();
		user.setContactNumber("1234567890");
		orderDto.setUser(user);

		Product mockBananaBread = Mockito.mock(Product.class);
		Mockito.when(mockBananaBread.getUnitPrice()).thenReturn(BigDecimal.valueOf(165));
		Mockito.when(productRepository.findById(991l)).thenReturn(Optional.of(mockBananaBread));

		DeliveryDate mockDeliveryDate = new DeliveryDate();
		mockDeliveryDate.setDate(TestUtils.convertLocalDateToDate(LocalDate.now()));
		mockDeliveryDate.setOrderLimit(6);
		mockDeliveryDate.setId(111l);
		Mockito.when(deliveryDateService.getDeliveryDate(ArgumentMatchers.any(Long.class))).thenReturn(mockDeliveryDate);
		Mockito.when(deliveryDateService.isDeliveryDateAvailable(ArgumentMatchers.any(Long.class))).thenReturn(true);
		
		OrderDetail mockSavedOrder = mockOrder();

		Mockito.when(orderRepository.save(Mockito.any(OrderDetail.class))).thenReturn(mockSavedOrder);

		OrderConfirmation result = orderService.order(orderDto);
		assertNotNull(result.getOrderNumber());

		assertEquals(OrderStatus.REGISTERED, result.getOrderStatus());

		ArgumentCaptor<OrderDetail> arg = ArgumentCaptor.forClass(OrderDetail.class);
		Mockito.verify(orderRepository).save(arg.capture());
		
		OrderDetail resultOrder = arg.getValue();
		assertEquals(DeliveryType.DELIVER, resultOrder.getDeliveryType());
		assertEquals(PaymentType.CASH, resultOrder.getPaymentType());
		assertNotNull(resultOrder.getOrderDate());
		assertEquals(1, resultOrder.getOrderItems().size());

		assertEquals(mockBananaBread, resultOrder.getOrderItems().get(0).getProduct());
		assertEquals(user.getContactNumber(), resultOrder.getUser().getContactNumber());
		assertEquals(address.getLine1(), resultOrder.getShipping().getAddressLineOne());
		assertEquals(new BigDecimal("165"), resultOrder.getTotal());
		assertEquals(mockDeliveryDate, resultOrder.getDeliveryDate());
	}
	
	@Test
	public void testGetOrdersNoFilter() {
		orderService.getOrders(Optional.empty());
		
		Mockito.verify(orderRepository).findAll(Mockito.any(Sort.class));
	}
	
	@Test
	public void testGetOrdersWithFilter() {
		orderService.getOrders(Optional.of(OrderStatus.REGISTERED));
		
		Mockito.verify(orderRepository).findAllByStatus(Mockito.eq(OrderStatus.REGISTERED), Mockito.any(Sort.class));
	}
	
	@Test
	void testUpdateNonExistingOrdersThenThrowsException() {
		Mockito.when(orderRepository.findById(Mockito.eq(Long.valueOf(1234l)))).thenReturn(Optional.empty());

		OrderUpdateDto orderDto = new OrderUpdateDto();
		assertThrows(ResourceNotFoundException.class, () -> {
			orderService.updateOrder(Long.valueOf(1234l), orderDto);
		});
	}
	
	@Test
	void testUpdateExistingOrderThenSave() {
		OrderDetail orderDetail = getOrderDetail();
		Mockito.when(orderRepository.findById(Long.valueOf(1234l))).thenReturn(Optional.of(orderDetail));
	
		OrderUpdateDto orderDto = new OrderUpdateDto();
		orderDto.setStatus(OrderStatus.COOKING);
		
		orderService.updateOrder(Long.valueOf(1234l), orderDto);
		
		ArgumentCaptor<OrderDetail> argCaptor = ArgumentCaptor.forClass(OrderDetail.class);
		Mockito.verify(orderRepository).save(argCaptor.capture());
		
		assertEquals(OrderStatus.COOKING, argCaptor.getValue().getTracking().getStatus());
	}
	
	@Test
	void testGetOrderNullId() {
		assertThrows(IllegalArgumentException.class, () -> orderService.getOrder(null));
	}
	
	@Test
	void testGetNonExistingOrderThenThrowsException() {
		Mockito.when(orderRepository.findById(Mockito.eq(Long.valueOf(1234l)))).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> {
			orderService.getOrder(1234l);
		});
	}
	
	@Test
	void testGetExistingOrder() {
		OrderDetail orderDetail = getOrderDetail();
		Mockito.when(orderRepository.findById(Long.valueOf(1234l))).thenReturn(Optional.of(orderDetail));
		
		OrderDetail order = orderService.getOrder(1234l);
		assertEquals(orderDetail, order);
	}
	
	private OrderDetail getOrderDetail() {
		OrderDetail orderDetail = new OrderDetail();
		OrderTracking tracking = new OrderTracking();
		tracking.setStatus(OrderStatus.REGISTERED);
		orderDetail.setTracking(tracking);
		return orderDetail;
	}

	private OrderDetail mockOrder() {
		return mockOrder(1234l);
	}

	private OrderDetail mockOrder(Long id) {
		OrderDetail mockSavedOrder = new OrderDetail();
		mockSavedOrder.setId(1234l);
		if (id != null) {
			mockSavedOrder.setId(id);
		}
		User userEntity = new User();
		userEntity.setContactNumber("01234");
		mockSavedOrder.setUser(userEntity);
		OrderTracking tracking = new OrderTracking();
		tracking.setStatus(OrderStatus.REGISTERED);
		mockSavedOrder.setTracking(tracking);
		OrderItem orderItem = new OrderItem(mockSavedOrder);
		orderItem.setBuyingPrice(BigDecimal.valueOf(165l));
		orderItem.setProductCount(1);
		orderItem.setTotal(BigDecimal.valueOf(165l));
		List<OrderItem> orderItems = new ArrayList<>();
		orderItems.add(orderItem);
		mockSavedOrder.setOrderItems(orderItems);
		return mockSavedOrder;
	}

}

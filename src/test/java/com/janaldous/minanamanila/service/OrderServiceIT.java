package com.janaldous.minanamanila.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.janaldous.minanamanila.data.AddressRepository;
import com.janaldous.minanamanila.data.OrderDetail;
import com.janaldous.minanamanila.data.OrderItemRepository;
import com.janaldous.minanamanila.data.OrderRepository;
import com.janaldous.minanamanila.data.OrderStatus;
import com.janaldous.minanamanila.data.OrderTracking;
import com.janaldous.minanamanila.data.OrderTrackingRepository;
import com.janaldous.minanamanila.data.Product;
import com.janaldous.minanamanila.data.ProductRepository;
import com.janaldous.minanamanila.data.User;
import com.janaldous.minanamanila.data.UserRepository;
import com.janaldous.minanamanila.testutil.OrderDtoMockFactory;
import com.janaldous.minanamanila.testutil.TestUtils;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;
import com.janaldous.minanamanila.webfacade.dto.OrderUpdateDto;

@SpringBootTest
class OrderServiceIT {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	private static boolean isDBInitialized = false;

	@Autowired
	private OrderTrackingRepository orderTrackingRepository;

	@BeforeEach
	public void beforeEach() {
		// make sure db is empty, from other tests
		dbCleanUp();
		if (!isDBInitialized) {
			initializeDB();
			isDBInitialized = true;
		}

		Product origBananaBread = productRepository.findByName("Original Banana Bread");
		assertThat(origBananaBread, is(not(nullValue())));
	}

	private void initializeDB() {
		// add orders
		for (int i = 0; i < 10; i++) {
			OrderDetail mockOrder = mockOrderDetail();
			mockOrder.setDeliveryDate(new Date());
			if (i % 2 == 1)
				mockOrder.getTracking().setStatus(OrderStatus.DELIVERED);
			else
				mockOrder.getTracking().setStatus(OrderStatus.REGISTERED);

			// random date between 90 days ago and now
			long timeStart = new Date().getTime() - TimeUnit.DAYS.toMillis(1) * 90;
			mockOrder.setOrderDate(TestUtils.between(new Date(timeStart), new Date()));

			userRepository.save(mockOrder.getUser());
			orderTrackingRepository.save(mockOrder.getTracking());
			orderRepository.save(mockOrder);
		}
		assertEquals(10, orderRepository.count());
	}

	private void dbCleanUp() {
		orderRepository.deleteAll();
		userRepository.deleteAll();
		addressRepository.deleteAll();
		orderItemRepository.deleteAll();
		orderTrackingRepository.deleteAll();
		isDBInitialized = false;
	}

	@Test
	void testCreateOrder() {
		assertEquals(10, orderRepository.count());
		assertEquals(10, userRepository.count());
		
		OrderDto input = getMockOrder();
		orderService.order(input);

		assertEquals(11, orderRepository.count());
		assertEquals(11, userRepository.count());
		assertEquals(1, addressRepository.count());
		assertEquals(1, orderItemRepository.count());
		assertEquals(11, orderTrackingRepository.count());

		// clean up
		dbCleanUp();
	}

	@Test
	void testCreateOrderInvalidDeliveryDate() {
		OrderDto input = getMockOrder();

		assertThrows(ResourceNotFoundException.class, () -> orderService.order(input));
		assertEquals(10, userRepository.count());
		assertEquals(0, addressRepository.count());
	}

	@Test
	void testGetOrdersSortedByOrderDate() {
		List<OrderDetail> orders = orderService.getOrders(Optional.empty());

		assertEquals(10, orders.size());
		for (int i = 0; i < 9; i++) {
			assertTrue(orders.get(i).getOrderDate().before(orders.get(i + 1).getOrderDate()));
		}
	}

	@Test
	void testGetOrdersFilterByOrderStatus() {
		List<OrderDetail> orders = orderService.getOrders(Optional.of(OrderStatus.REGISTERED));

		assertEquals(5, orders.size());
		for (int i = 0; i < 4; i++) {
			assertTrue(orders.get(i).getOrderDate().before(orders.get(i + 1).getOrderDate()));
			assertEquals(OrderStatus.REGISTERED, orders.get(i).getTracking().getStatus());
		}
	}

	@Test
	void testUpdateOrderThenThrowValidationException() {
		OrderUpdateDto orderUpdate = new OrderUpdateDto();
		orderUpdate.setStatus(null);

		assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(Long.valueOf(1234l), orderUpdate));
	}

	private static OrderDetail mockOrderDetail() {
		OrderDetail mockSavedOrder = new OrderDetail();
		User userEntity = new User();
		userEntity.setFirstName("Mickey");
		userEntity.setLastName("Mouse");
		userEntity.setContactNumber("01234");
		mockSavedOrder.setUser(userEntity);
		OrderTracking tracking = new OrderTracking();
		tracking.setStatus(OrderStatus.REGISTERED);
		mockSavedOrder.setTracking(tracking);
		return mockSavedOrder;
	}

	private OrderDto getMockOrder() {
		return OrderDtoMockFactory.factory();
	}

}

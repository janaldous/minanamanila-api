package com.janaldous.minanamanila.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.janaldous.minanamanila.data.AddressRepository;
import com.janaldous.minanamanila.data.DeliveryDate;
import com.janaldous.minanamanila.data.OrderDetail;
import com.janaldous.minanamanila.data.OrderItem;
import com.janaldous.minanamanila.data.OrderRepository;
import com.janaldous.minanamanila.data.OrderStatus;
import com.janaldous.minanamanila.data.OrderTracking;
import com.janaldous.minanamanila.data.OrderTrackingRepository;
import com.janaldous.minanamanila.data.Product;
import com.janaldous.minanamanila.data.ProductRepository;
import com.janaldous.minanamanila.data.UserRepository;
import com.janaldous.minanamanila.domain.mapper.OrderConfirmationMapper;
import com.janaldous.minanamanila.domain.mapper.OrderItemMapper;
import com.janaldous.minanamanila.domain.mapper.OrderMapper;
import com.janaldous.minanamanila.webfacade.dto.OrderConfirmation;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;
import com.janaldous.minanamanila.webfacade.dto.OrderUpdateDto;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderTrackingRepository orderTrackingRepository;

	@Autowired
	private DeliveryDateService deliveryDateService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Transactional
	public OrderConfirmation order(OrderDto orderDto) {
		OrderDetail orderDetail = OrderMapper.toEntity(orderDto);
		orderDetail.setOrderDate(new Date());

		if (!deliveryDateService.isDeliveryDateAvailable(orderDto.getDeliveryDateId()))
			throw new OrderException("Order limit exeeded");

		// set delivery date
		DeliveryDate deliveryDate = deliveryDateService.getDeliveryDate(orderDto.getDeliveryDateId());
		orderDetail.setDeliveryDate(deliveryDate);

		// set products
		List<OrderItem> orderItems = orderDto.getProducts().stream().map(productDto -> {
			Product product = productRepository.findById(productDto.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Product not found with id: " + productDto.getId()));
			return orderItemMapper.toEntity(product, productDto.getQuantity(), orderDetail);
		}).collect(Collectors.toList());
		orderDetail.setOrderItems(orderItems);

		// set sum
		BigDecimal total = orderDetail.getOrderItems().stream().map(x -> x.getBuyingPrice()).reduce(BigDecimal.ZERO,
				(subtotal, element) -> subtotal.add(element));
		orderDetail.setTotal(total);

		// save transitive entities
		addressRepository.save(orderDetail.getShipping());
		userRepository.save(orderDetail.getUser());

		// set tracking
		OrderTracking tracking = new OrderTracking();
		tracking.setStatus(OrderStatus.REGISTERED);
		orderTrackingRepository.save(tracking);
		orderDetail.setTracking(tracking);

		OrderDetail savedOrder = orderRepository.save(orderDetail);

		return OrderConfirmationMapper.toDto(savedOrder);
	}

	public List<OrderDetail> getOrders(Optional<OrderStatus> status) {
		if (!status.isPresent()) {
			return orderRepository.findAll(Sort.by("orderDate"));
		}
		return orderRepository.findAllByStatus(status.get(), Sort.by("orderDate"));
	}

	public OrderDetail updateOrder(Long id, @Valid OrderUpdateDto orderDto) {
		Optional<OrderDetail> optOrder = orderRepository.findById(id);
		if (!optOrder.isPresent())
			throw new ResourceNotFoundException("Order with id: " + id + " was not found");

		OrderDetail orderDetail = optOrder.get();
		orderDetail.getTracking().setStatus(orderDto.getStatus());

		return orderRepository.save(orderDetail);
	}

	public OrderDetail getOrder(Long id) {
		if (id == null)
			throw new IllegalArgumentException();

		Optional<OrderDetail> optOrder = orderRepository.findById(id);
		if (!optOrder.isPresent())
			throw new ResourceNotFoundException("Order with id: " + id + " was not found");

		return optOrder.get();
	}

}

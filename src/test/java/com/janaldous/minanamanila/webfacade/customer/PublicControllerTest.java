package com.janaldous.minanamanila.webfacade.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.janaldous.minanamanila.service.OrderService;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;

public class PublicControllerTest {

	@Mock
	private OrderService orderService;
	
	@InjectMocks
	private PublicController publicController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testOrderValidation() {
		OrderDto order = new OrderDto();
		publicController.order(order);
	}

	
}

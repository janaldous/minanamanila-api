package com.janaldous.minanamanila.webfacade.admin;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.janaldous.minanamanila.service.OrderService;
import com.janaldous.minanamanila.webfacade.dto.OrderUpdateDto;

class OrderControllerTest {

	@Mock
	private OrderService orderService;

	@InjectMocks
	private OrderController orderController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testUpdateOrderValidId() {
		OrderUpdateDto orderDto = new OrderUpdateDto();
		orderController.updateOrder("1200", orderDto);
	}

	@Test
	void testUpdateOrderNonNumberId() {
		OrderUpdateDto orderDto = new OrderUpdateDto();
		assertThrows(NumberFormatException.class, () -> {
			orderController.updateOrder("adbbd", orderDto);
		});
	}

}

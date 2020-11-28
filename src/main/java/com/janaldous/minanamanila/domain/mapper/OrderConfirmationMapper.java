package com.janaldous.minanamanila.domain.mapper;

import com.janaldous.minanamanila.data.OrderDetail;
import com.janaldous.minanamanila.webfacade.dto.OrderConfirmation;

public class OrderConfirmationMapper {

	public static OrderConfirmation toDto(OrderDetail input) {
		if (input == null)
			throw new IllegalArgumentException();
		if (input.getTracking() == null)
			throw new IllegalArgumentException("tracking must not be null");

		OrderConfirmation output = new OrderConfirmation();
		output.setOrderNumber(input.getId());
		output.setUser(UserMapper.toDto(input.getUser()));
		output.setOrderStatus(input.getTracking().getStatus());
		if (input.getDeliveryDate() != null) {
			output.setDeliveryDate(input.getDeliveryDate().getDate());
		}

		return output;
	}

}

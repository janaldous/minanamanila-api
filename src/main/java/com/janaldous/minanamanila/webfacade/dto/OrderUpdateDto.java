package com.janaldous.minanamanila.webfacade.dto;

import javax.validation.constraints.NotNull;

import com.janaldous.minanamanila.data.OrderStatus;

import lombok.Data;

@Data
public class OrderUpdateDto {

	@NotNull
	private OrderStatus status;
	
}

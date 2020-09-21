package com.janaldous.breadforyouph.webfacade.dto;

import javax.validation.constraints.NotNull;

import com.janaldous.breadforyouph.data.OrderStatus;

import lombok.Data;

@Data
public class OrderUpdateDto {

	@NotNull
	private OrderStatus status;
	
}

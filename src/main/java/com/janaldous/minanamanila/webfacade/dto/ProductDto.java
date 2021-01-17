package com.janaldous.minanamanila.webfacade.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ProductDto {

	@NotNull
	private Long id;
	@NotNull
	private Integer quantity;
	
}

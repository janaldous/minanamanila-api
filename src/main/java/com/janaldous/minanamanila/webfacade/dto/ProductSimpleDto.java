package com.janaldous.minanamanila.webfacade.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.janaldous.minanamanila.data.Category;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductSimpleDto {

	private Long id;
	private String code;
	private String name;
	private String description;
	private BigDecimal unitPrice;
	private BigDecimal srp;
	private String brand;
	private Set<Category> categories;
	private String imageURL;

}
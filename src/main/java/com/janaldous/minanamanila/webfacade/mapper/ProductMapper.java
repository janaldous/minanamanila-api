package com.janaldous.minanamanila.webfacade.mapper;

import org.springframework.stereotype.Component;

import com.janaldous.minanamanila.data.Product;
import com.janaldous.minanamanila.webfacade.dto.ProductSimpleDto;

@Component
public class ProductMapper {

	public ProductSimpleDto toProductSimpleDto(Product product) {
		return ProductSimpleDto.builder().id(product.getId()).code(product.getCode())
				.description(product.getDescription()).name(product.getName()).unitPrice(product.getUnitPrice())
				.srp(product.getSrp()).categories(product.getCategories()).build();
	}

}

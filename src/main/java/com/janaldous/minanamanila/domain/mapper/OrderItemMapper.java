package com.janaldous.minanamanila.domain.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.janaldous.minanamanila.data.OrderDetail;
import com.janaldous.minanamanila.data.OrderItem;
import com.janaldous.minanamanila.data.Product;

@Component
public class OrderItemMapper {

	public OrderItem toEntity(Product product, int quantity, OrderDetail orderDetail) {
		if (product == null || quantity <= 0 || orderDetail == null)
			throw new IllegalArgumentException("quantity must be greater than zero and order must not be null");

		OrderItem item = new OrderItem(orderDetail);
		item.setProductCount(quantity);
		item.setProduct(product);
		item.setBuyingPrice(product.getUnitPrice());
		item.setTotal(product.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));

		return item;
	}

}

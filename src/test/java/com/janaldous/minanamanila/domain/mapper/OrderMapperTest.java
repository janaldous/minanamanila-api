package com.janaldous.minanamanila.domain.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.janaldous.minanamanila.data.DeliveryType;
import com.janaldous.minanamanila.data.OrderDetail;
import com.janaldous.minanamanila.data.PaymentType;
import com.janaldous.minanamanila.testutil.ProductDtoMockFactory;
import com.janaldous.minanamanila.webfacade.dto.AddressDto;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;
import com.janaldous.minanamanila.webfacade.dto.UserDto;

class OrderMapperTest {

	@Test
	void test() {
		OrderDto orderDto = new OrderDto();
		UserDto user = new UserDto();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setContactNumber("123456789");
		user.setEmail("john.doe@example.com");
		orderDto.setUser(user);
		AddressDto address = new AddressDto();
		orderDto.setAddress(address);
		orderDto.setDeliveryType(DeliveryType.DELIVER);
		orderDto.setPaymentType(PaymentType.CASH);
		orderDto.setProducts(ProductDtoMockFactory.getMockProducts());
		Date today = new Date();
		orderDto.setDeliveryDate(today);

		OrderDetail result = OrderMapper.toEntity(orderDto);
		assertEquals(user.getContactNumber(), result.getUser().getContactNumber());
		assertEquals(user.getFirstName(), result.getUser().getFirstName());
		assertEquals(address.getLine1(), result.getShipping().getAddressLineOne());
		assertEquals(orderDto.getDeliveryType(), result.getDeliveryType());
		assertEquals(orderDto.getPaymentType(), result.getPaymentType());
		assertEquals(null, result.getOrderItems());
		assertEquals(orderDto.getDeliveryDate(), result.getDeliveryDate());
	}

	@Test
	void testNullObject() {
		assertNull(AddressMapper.toEntity(null));
	}

}

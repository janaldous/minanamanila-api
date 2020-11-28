package com.janaldous.minanamanila.domain.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.janaldous.minanamanila.data.DeliveryDate;
import com.janaldous.minanamanila.webfacade.dto.DeliveryDateDto;

class DeliveryDateMapperTest {

	@Test
	void test() {
		DeliveryDateDto dto = new DeliveryDateDto();
		dto.setDate(new Date());
		dto.setOrderLimit(6);
		
		DeliveryDate result = DeliveryDateMapper.toEntity(dto);
		
		assertEquals(6, result.getOrderLimit());
		assertEquals(dto.getDate(), result.getDate());
	}

}

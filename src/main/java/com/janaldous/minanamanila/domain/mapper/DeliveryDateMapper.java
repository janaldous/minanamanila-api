package com.janaldous.minanamanila.domain.mapper;

import com.janaldous.minanamanila.data.DeliveryDate;
import com.janaldous.minanamanila.webfacade.dto.DeliveryDateDto;

public class DeliveryDateMapper {

	public static DeliveryDate toEntity(DeliveryDateDto input) {
		DeliveryDate output = new DeliveryDate();
		output.setDate(input.getDate());
		output.setOrderLimit(input.getOrderLimit());
		
		return output;
	}

}

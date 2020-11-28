package com.janaldous.minanamanila.domain.mapper;

import com.janaldous.minanamanila.data.Address;
import com.janaldous.minanamanila.webfacade.dto.AddressDto;

public class AddressMapper {

	public static Address toEntity(AddressDto input) {
		if (input == null) return null;
		
		Address output = new Address();
		
		output.setAddressLineOne(input.getLine1());
		output.setAddressLineTwo(input.getVillage());
		output.setCity(input.getCity());
		output.setPostalCode(input.getPostcode());
		output.setProvince(input.getProvince());
		output.setCountry("Philippines");
		output.setSpecialInstructions(input.getSpecialInstructions());
		
		return output; 
	}
	
}

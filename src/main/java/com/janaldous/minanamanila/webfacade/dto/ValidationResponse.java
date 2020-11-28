package com.janaldous.minanamanila.webfacade.dto;

import java.util.Map;

import lombok.Value;

@Value
public class ValidationResponse {

	private Map<String, String> validation;
	
}

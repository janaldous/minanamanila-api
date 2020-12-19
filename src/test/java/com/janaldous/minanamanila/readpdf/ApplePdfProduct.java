package com.janaldous.minanamanila.readpdf;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ApplePdfProduct {

	private String name;
	private String color;
	private BigDecimal srp;
	private BigDecimal discount;
	private BigDecimal lbd;
	private String leadTime;
	private String warranty;
	
}

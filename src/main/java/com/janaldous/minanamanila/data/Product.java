package com.janaldous.minanamanila.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Entity
@Data
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull
	private Long id;

	private String code;

	@NotBlank(message = "Please enter the product name")
	@NotNull
	private String name;

	/** Description can also include product model information **/
	@NotBlank(message = "Please enter the description")
	@NotNull
	private String description;

	@Column(name = "unit_price")
	@Min(value = 1, message = "Please select at least one value")
	@NotNull
	private BigDecimal unitPrice;

	@Column(name = "srp")
	@Min(value = 1, message = "Please select at least one value")
	private BigDecimal srp;

	@NotBlank(message = "Please enter the brand name")
	@NotNull
	private String brand;

	@ManyToMany(cascade = { CascadeType.ALL })
	private Set<Category> categories;
	
	@ManyToMany(cascade = { CascadeType.ALL })
	@JsonSerialize(using = ColorSetSerializer.class)
	private Set<ProductColor> colors;

	@Column(name = "picture_url")
	@NotNull
	private String pictureUrl;

}

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

import lombok.Data;

@Entity
@Data
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String code;
	
	@NotBlank(message = "Please enter the product name")
	private String name;
	
	/** Description can also include product model information **/
	@NotBlank(message = "Please enter the description")
	private String description;
	
	@Column(name = "unit_price")
	@Min(value = 1, message = "Please select at least one value")
	private BigDecimal unitPrice;
	
	@Column(name = "unit_price")
	@Min(value = 1, message = "Please select at least one value")
	private BigDecimal srp;
	
	@NotBlank(message = "Please enter the brand name")
	private String brand;
	
	@ManyToMany(cascade = { CascadeType.ALL })
    private Set<Category> categories;
	
}

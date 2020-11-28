package com.janaldous.minanamanila.data;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Category {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank(message = "Please enter a category name")
	private String name;
	
	@ManyToMany(mappedBy = "categories")
    private Set<Product> products;
}

package com.janaldous.minanamanila.data;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

@Entity
@Data
public class ProductColor {
	
	@Id
	@Column(name = "user_id")
	private Long id;
	
	private String color;
	
	@ManyToMany(mappedBy = "categories")
    private Set<Product> products;

}

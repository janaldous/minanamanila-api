package com.janaldous.minanamanila.data;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@NoArgsConstructor
@Entity
public class ProductColor {

	@Id
	@GeneratedValue
	@NonNull
	@Getter
	private Long id;

	@NonNull
	@Getter
	private String color;
	
	@NonNull
	@Getter
	@Column(name = "product_code")
	private String productCode;

	@ManyToMany(mappedBy="colors")
	@Getter(onMethod_ = @JsonIgnore)
	private Set<Product> products;

}

package com.janaldous.minanamanila.data;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Setter
public class Category {

	@Id
	@GeneratedValue
	@Getter
	private Long id;

	@NotBlank(message = "Please enter a category name")
	@Getter
	private String name;

	@ManyToMany(mappedBy = "categories")
	@Getter(onMethod_ = @JsonIgnore)
	private Set<Product> products;
}

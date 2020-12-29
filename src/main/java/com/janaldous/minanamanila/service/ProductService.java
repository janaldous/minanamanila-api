package com.janaldous.minanamanila.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.janaldous.minanamanila.data.Product;
import com.janaldous.minanamanila.data.ProductRepository;

@Component
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	public Page<Product> getProducts(int page, int size) {
		Pageable pageRequest = PageRequest.of(page, size);
		return productRepository.findAll(pageRequest);
	}

	public Optional<Product> getProduct(Long id) {
		return productRepository.findById(id);
	}

}

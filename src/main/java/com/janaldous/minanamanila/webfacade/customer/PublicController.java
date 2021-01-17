package com.janaldous.minanamanila.webfacade.customer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.janaldous.minanamanila.data.Product;
import com.janaldous.minanamanila.service.OrderService;
import com.janaldous.minanamanila.service.ProductService;
import com.janaldous.minanamanila.webfacade.dto.OrderConfirmation;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api")
public class PublicController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;
	
	@PostMapping("/order")
	public @ResponseBody ResponseEntity<OrderConfirmation> order(@Valid @RequestBody OrderDto orderDto) {
		return new ResponseEntity<OrderConfirmation>(orderService.order(orderDto), HttpStatus.CREATED);
	}

	@GetMapping("/products")
	public @ResponseBody Page<Product> getProducts(@RequestParam("page") int page, @RequestParam("size") int size) {
		return productService.getProducts(page, size);
	}

	@GetMapping("/products/{id}")
	public @ResponseBody ResponseEntity<Product> getProduct(@PathVariable Long id) {
		return productService.getProduct(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@ApiOperation(value = "Get product suggestions for a particular product")
	@GetMapping("/products/suggestions")
	public @ResponseBody Page<Product> getProductSuggestions(@RequestParam Long id) {
		return productService.getProducts(0, 6);
	}

}

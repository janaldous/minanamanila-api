package com.janaldous.minanamanila.webfacade.customer;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.janaldous.minanamanila.data.DeliveryDate;
import com.janaldous.minanamanila.service.DeliveryDateService;
import com.janaldous.minanamanila.service.OrderService;
import com.janaldous.minanamanila.service.ProductService;
import com.janaldous.minanamanila.service.ResourceNotFoundException;
import com.janaldous.minanamanila.webfacade.dto.OrderConfirmation;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;
import com.janaldous.minanamanila.webfacade.dto.ProductSimpleDto;
import com.janaldous.minanamanila.webfacade.mapper.ProductMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PublicController {

	@Autowired
	private DeliveryDateService deliveryService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductMapper productMapper;

	@GetMapping("/delivery")
	public @ResponseBody List<DeliveryDate> getDeliveryDates(@RequestParam("page") int page,
			@RequestParam("size") int size) {

		Page<DeliveryDate> resultPage = deliveryService.getDeliveryDates(page, size);
		if (page >= resultPage.getTotalPages()) {
			throw new ResourceNotFoundException(
					"Cannot find delivery dates with params: page=" + page + " size=" + size);
		}

		return resultPage.getContent();
	}

	@PostMapping("/order")
	public @ResponseBody ResponseEntity<OrderConfirmation> order(@Valid @RequestBody OrderDto orderDto) {
		return new ResponseEntity<OrderConfirmation>(orderService.order(orderDto), HttpStatus.CREATED);
	}

	@GetMapping("/products")
	public @ResponseBody List<ProductSimpleDto> getProducts(@RequestParam("page") int page, @RequestParam("size") int size) {
		return productService.getProducts(page, size).getContent().stream()
				.map(product -> productMapper.toProductSimpleDto(product))
				.collect(Collectors.toList());
	}

	@GetMapping("/products/{id}")
	public @ResponseBody ResponseEntity<ProductSimpleDto> getProduct(@PathVariable Long id) {
		return productService.getProduct(id)
				.map(product -> ResponseEntity.ok(productMapper.toProductSimpleDto(product)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/products/photo/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody ResponseEntity<byte[]> getProductPhoto(@PathVariable Long id) {
		return productService.getProduct(id).map(product -> ResponseEntity.ok(product.getPhoto()))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@ApiOperation(value = "Get product suggestions for a particular product")
	@GetMapping("/products/suggestions")
	public @ResponseBody List<ProductSimpleDto> getProductSuggestions(@RequestParam Long id) {
		return productService.getProducts(0, 6).getContent().stream()
				.map(product -> productMapper.toProductSimpleDto(product))
				.collect(Collectors.toList());
	}

}

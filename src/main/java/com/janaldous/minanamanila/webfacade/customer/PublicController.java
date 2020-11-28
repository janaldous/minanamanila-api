package com.janaldous.minanamanila.webfacade.customer;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.janaldous.minanamanila.data.DeliveryDate;
import com.janaldous.minanamanila.data.Product;
import com.janaldous.minanamanila.service.DeliveryDateService;
import com.janaldous.minanamanila.service.OrderService;
import com.janaldous.minanamanila.service.ProductService;
import com.janaldous.minanamanila.service.ResourceNotFoundException;
import com.janaldous.minanamanila.webfacade.dto.OrderConfirmation;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/api")
public class PublicController {

	@Autowired
	private DeliveryDateService deliveryService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

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
	public @ResponseBody List<Product> getProducts(@RequestParam("page") int page, @RequestParam("size") int size) {
		return productService.getProducts(page, size).getContent();
	}
}

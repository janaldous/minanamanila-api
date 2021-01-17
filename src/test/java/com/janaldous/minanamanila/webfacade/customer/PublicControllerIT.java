package com.janaldous.minanamanila.webfacade.customer;

import static org.hamcrest.Matchers.containsString;

import java.util.Date;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janaldous.minanamanila.data.DeliveryType;
import com.janaldous.minanamanila.data.PaymentType;
import com.janaldous.minanamanila.service.OrderService;
import com.janaldous.minanamanila.testutil.ProductDtoMockFactory;
import com.janaldous.minanamanila.webfacade.ExceptionTranslator;
import com.janaldous.minanamanila.webfacade.dto.AddressDto;
import com.janaldous.minanamanila.webfacade.dto.OrderDto;
import com.janaldous.minanamanila.webfacade.dto.UserDto;

@Tag("IntegrationTest")
@WebMvcTest(PublicController.class)
@Import({ ExceptionTranslator.class })
public class PublicControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper mapper;

	@Test
	public void testOrderInvalidUser() throws Exception {

		OrderDto orderMock = getMockOrder();
		orderMock.setUser(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/order").content(mapper.writeValueAsString(orderMock))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.validation.user", containsString("must not be null")))
				.andReturn();
	}

	@Test
	public void testOrderNullUserContactNumber() throws Exception {

		OrderDto orderMock = getMockOrder();
		orderMock.getUser().setContactNumber(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/order").content(mapper.writeValueAsString(orderMock))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers
						.jsonPath("$.validation['user.contactNumber']", containsString("Invalid mobile number")))
				.andReturn();

	}

	@Test
	public void testOrderInvalidUserContactNumber() throws Exception {

		OrderDto orderMock = getMockOrder();
		orderMock.getUser().setContactNumber("011212121");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/order").content(mapper.writeValueAsString(orderMock))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers
						.jsonPath("$.validation['user.contactNumber']", containsString("Invalid mobile number")))
				.andReturn();

	}

	@Test
	public void testOrderInvalidAddress() throws Exception {

		OrderDto orderMock = getMockOrder();
		orderMock.setAddress(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/order").content(mapper.writeValueAsString(orderMock))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.validation.address", containsString("must not be null")))
				.andReturn();
	}

	@Test
	public void testOrderInvalidAddressLine1() throws Exception {

		OrderDto orderMock = getMockOrder();
		orderMock.getAddress().setLine1(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/order").content(mapper.writeValueAsString(orderMock))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.validation['orderDto']",
						containsString("Address fields should not be null when Delivery is chosen")))
				.andReturn();

	}

	@Test
	public void testValidOrder() throws Exception {

		OrderDto orderMock = getMockOrder();
		orderMock.setDeliveryDate(new Date());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/order").content(mapper.writeValueAsString(orderMock))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
	}

	private OrderDto getMockOrder() {
		OrderDto orderMock = new OrderDto();
		AddressDto address = new AddressDto();
		address.setLine1("Main Street");
		address.setVillage("Mickey Mouse Clubhouse");
		address.setCity("Sta Rosa");
		address.setProvince("Murica");
		address.setPostcode("4026");
		orderMock.setAddress(address);
		orderMock.setDeliveryType(DeliveryType.DELIVER);
		orderMock.setDeliveryDate(new Date());
		UserDto user = new UserDto();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setContactNumber("09123456789");
		orderMock.setUser(user);
		orderMock.setPaymentType(PaymentType.CASH);
		orderMock.setProducts(ProductDtoMockFactory.getMockProducts());
		return orderMock;
	}
}

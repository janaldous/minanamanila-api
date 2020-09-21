package com.janaldous.breadforyouph.webfacade.admin;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janaldous.breadforyouph.data.OrderDetail;
import com.janaldous.breadforyouph.data.OrderStatus;
import com.janaldous.breadforyouph.service.OrderService;
import com.janaldous.breadforyouph.service.ResourceNotFoundException;
import com.janaldous.breadforyouph.testutil.ApiTokenGetterUtil;
import com.janaldous.breadforyouph.webfacade.ExceptionTranslator;
import com.janaldous.breadforyouph.webfacade.dto.OrderUpdateDto;

@Tag("IntegrationTest")
@WebMvcTest(OrderController.class)
@Import({ ExceptionTranslator.class, ApiTokenGetterUtil.class })
public class OrderControllerIT {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OrderControllerIT.class);

	@MockBean
	private OrderService orderService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ApiTokenGetterUtil apiTokenGetterUtil;

	private String token;

	@BeforeEach
	public void beforeEach() throws JSONException {
		if (token == null) {
			token = apiTokenGetterUtil.getManagementApiToken();
		}
	}

	@Test
	public void testGetAllOrders() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/order").accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	public void testUpdateNonExistingOrder() throws Exception {

		Mockito.when(orderService.updateOrder(Mockito.anyLong(), Mockito.any(OrderUpdateDto.class)))
				.thenThrow(ResourceNotFoundException.class);

		OrderUpdateDto orderUpdate = new OrderUpdateDto();
		orderUpdate.setStatus(OrderStatus.COOKING);

		mockMvc.perform(MockMvcRequestBuilders.put("/admin/order/1234").content(mapper.writeValueAsString(orderUpdate))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
	}

	@Test
	public void testUpdateInvalidOrderThenThrowBadRequest() throws Exception {

		OrderUpdateDto orderUpdate = new OrderUpdateDto();
		orderUpdate.setStatus(null);

		mockMvc.perform(MockMvcRequestBuilders.put("/admin/order/1234").content(mapper.writeValueAsString(orderUpdate))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token))
				.andExpect(MockMvcResultMatchers.jsonPath("$.validation.status", containsString("must not be null")))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
	}

	@Test
	public void testGetOrder() throws Exception {
		OrderDetail mockOrderDetail = new OrderDetail();
		mockOrderDetail.setId(1234l);
		Mockito.when(orderService.getOrder(Mockito.anyLong())).thenReturn(mockOrderDetail);

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/order/1234").accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1234))).andReturn();
	}

}

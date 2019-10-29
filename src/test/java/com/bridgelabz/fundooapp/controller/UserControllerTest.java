package com.bridgelabz.fundooapp.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bridgelabz.fundooapp.controller.UserController;
import com.bridgelabz.fundooapp.dto.RegisterDTO;
import com.bridgelabz.fundooapp.model.User;
import com.bridgelabz.fundooapp.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	public void testRegister() throws Exception {
		User mockUser = new User();
		mockUser.setId(1L);
		mockUser.setEmailId("abc@test.com");
		mockUser.setPassword("password");
		mockUser.setFirstName("abc");
		mockUser.setLastName("pqr");
		mockUser.setMobileNumber(7978675991L);
		mockUser.setVerified(false);
		LocalDateTime date = LocalDateTime.now();
		mockUser.setCreatedDate(date);
		mockUser.setUpdatedDate(date);

		String bodyJSON = "{\n" + "    \"emailId\": \"abc@test.com\",\n" + "    \"firstName\": \"abc\",\n"
				+ "    \"lastName\": \"pqr\",\n" + "    \"password\": \"password\",\n"
				+ "    \"mobileNumber\": 7978675991\n" + "}";
		Mockito.when(userService.register(Mockito.any(RegisterDTO.class))).thenReturn(mockUser);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/fundoo/register")
				.accept(MediaType.APPLICATION_JSON).content(bodyJSON).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse httpResponse = result.getResponse();

		assertEquals(200, httpResponse.getStatus());

	}

}

package com.bridgelabz.fundooapp.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundooapp.dto.LoginDTO;
import com.bridgelabz.fundooapp.dto.RegisterDTO;
import com.bridgelabz.fundooapp.dto.ResetPasswordDTO;
import com.bridgelabz.fundooapp.dto.UserDetails;
import com.bridgelabz.fundooapp.model.User;
import com.bridgelabz.fundooapp.response.Response;
import com.bridgelabz.fundooapp.service.UserService;

@RestController
@RequestMapping("/fundoo")
@CrossOrigin(origins="http://localhost:4200",exposedHeaders= {"jwt_token"})

public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody RegisterDTO registerDTO) {
		userService.register(registerDTO);
		Response response = new Response("Registered Successfully. Please check your email to verify yourself", 200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> verifyToken(@PathVariable String token) {
		String message = userService.verifyToken(token);
		Response response = new Response(message, 200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDTO loginDTO, HttpServletResponse httpResponse) {
		UserDetails userDetails = userService.login(loginDTO);
//		httpResponse.addHeader("jwt_token", token);
		Response response = new Response("Successfully Logged in.", 200, userDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam String emailId) {
		String message = userService.forgetPassword(emailId);
		Response response = new Response(message, 202);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PutMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@PathVariable String token,
			@RequestBody ResetPasswordDTO resetPasswordDTO) {
		String message = userService.resetPassword(resetPasswordDTO, token);
		Response response = new Response(message, 202);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/email/{emailId}")
	public ResponseEntity<Response>  getUserByEmailId(@PathVariable String emailId, @RequestHeader("jwt_token") String token)
	{
		Long userId = userService.getUserByEmailId(emailId, token);
		Response response = new Response("User found", 200, userId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
//	@GetMapping("/getUserById")
//	public ResponseEntity<Response> getUserById(@RequestParam("userId") Long userId, @RequestHeader("jwt_token") String token) {
//		User user= userService.getUserById(userId,token);
//		Response response = new Response(message, 202);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
}

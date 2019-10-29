package com.bridgelabz.fundooapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundooapp.response.Response;

@ControllerAdvice
public class ExceptionAdviser {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> exceptionResolver(Exception exception) {

		Response response = new Response(exception.getMessage(), 500);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<Response> exceptionResolver(UserException exception) {

		Response response = new Response(exception.getMessage(), exception.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}

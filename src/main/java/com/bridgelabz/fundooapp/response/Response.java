package com.bridgelabz.fundooapp.response;

import lombok.Data;

@Data
public class Response {

	private String statusMessage;
	private int statusCode;
	private Object body;

	public Response(String statusMessage, int statusCode) {
		this.statusMessage = statusMessage;
		this.statusCode = statusCode;
	}

	public Response(String statusMessage, int statusCode, Object body) {
		this.statusMessage = statusMessage;
		this.statusCode = statusCode;
		this.body = body;
	}

}

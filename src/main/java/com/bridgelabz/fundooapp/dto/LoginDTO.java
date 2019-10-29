package com.bridgelabz.fundooapp.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginDTO {

	@NotNull
	private String emailId;
	@NotNull
	private String password;
}

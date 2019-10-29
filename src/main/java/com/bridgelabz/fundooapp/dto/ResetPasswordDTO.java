package com.bridgelabz.fundooapp.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ResetPasswordDTO {

	@NotNull
	private String password;
	@NotNull
	private String confirmPassword;
}

package com.bridgelabz.fundooapp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class RegisterDTO {

	@NotNull(message = "Email can't be empty")
	@Email(regexp = "^[A-Za-z0-9]+@*(\\.[_A-Za-z0-9-]+)*@*[A-Za-z0-9-]+(\\.(?:[A-Z]{2,}|com|org))+$", message = "Email Not valid")
	private String emailId;

	@NotNull(message = "First Name can't be empty")
	private String firstName;

	private String lastName;

	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", message = "Password must contain Capital letter, Small letter, One number")
	@NotNull(message = "Password can't be empty")
	private String password;

	@Pattern(regexp = "[0-9]{10}", message = "Mobile number is only in digits")
	@NotNull
	private Long mobileNumber;
}

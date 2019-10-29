package com.bridgelabz.fundooapp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, unique = true)
	private String emailId;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private Long mobileNumber;

	private boolean isVerified;

	@Column(nullable = false)
	@DateTimeFormat
	private LocalDateTime createdDate;

	@Column(nullable = false)
	@DateTimeFormat
	private LocalDateTime updatedDate;

}

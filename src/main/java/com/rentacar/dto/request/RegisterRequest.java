package com.rentacar.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	@Size(max = 50)
	@NotBlank(message = "Please provide your First Name")
	private String firstName;
	@Size(max = 50)
	@NotBlank(message = "Please provide your Last Name")
	private String lastName;
	@Size(max = 50)
	@Email(message = "Please provide valid email")
	private String email;
	@Size(min=4,max = 20,message = "Please provide CorrectSize of Password")
	@NotBlank(message = "Please provide your password")
	private String password;
	@Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")
	@Size(min=14,max=14)
	@NotBlank(message = "Please provide your phone number")
	private String phoneNumber;
	@Size(max=100)
	@NotBlank(message = "Please provide your address")
	private String address;
	@Size(max=15)
	@NotBlank(message = "Please provide your zipcode")
	private String zipcode;

	
}

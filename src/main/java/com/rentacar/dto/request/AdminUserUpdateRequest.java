package com.rentacar.dto.request;

import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest {

	

	@Size(max=50)
	@NotBlank(message = "Please provide your first name")
	private String firstName;
	@Size(max=50)
	@NotBlank(message = "Please provide your last name")
	private String lastName;
	@Size(max=50)
	@Email(message = "Please provide your email")
	private String email;
	@Size(min=4,max=120)
	@NotBlank(message = "Please provide your password")
	private String password;
	@Size(min=14,max=14)
	@NotBlank(message = "Please provide your phone number")
	private String phoneNumber;
	@Size(max=50)
	@NotBlank(message = "Please provide your address")
	private String address;
	@Size(max=50)
	@NotBlank(message = "Please provide your zipcode")
	private String zipcode;

	
	private Boolean builtIn;
	
	
	private Set<String> roles;
}

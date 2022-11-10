package com.rentacar.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminPasswordRequest {

	@Size(min=4,max=120,message = "Please provide your old password")
	@NotBlank(message = "Please provide your old password")
	private String oldPassword;
	@Size(min=4,max=120,message = "Please provide your new password")
	@NotBlank(message = "Please provide your new password")
	private String newPassword;
	
}

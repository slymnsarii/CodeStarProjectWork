package com.rentacar.dto;

import java.util.HashSet;
import java.util.Set;

import com.rentacar.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String phoneNumber;
	private String address;
	private String zipcode;
	private Boolean builtIn;
	
	private Set<String> roles;
	
	
	
	public void setRoles(Set<Role> roles) {
		Set<String> rolesStr=new HashSet<>();
		roles.forEach(role->
		{rolesStr.add(role.getType().getName());
		});
		this.roles=rolesStr;
	}
	

	
}

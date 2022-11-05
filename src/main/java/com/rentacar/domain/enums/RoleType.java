package com.rentacar.domain.enums;

public enum RoleType {

	ROLE_ADMİN("Administrator"),
	ROLE_CUSTOMER("Customer");
	
	private String name;

	private RoleType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	
	
	
	
	
}

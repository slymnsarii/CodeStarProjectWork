package com.rentacar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentacar.domain.Role;
import com.rentacar.domain.enums.RoleType;
import com.rentacar.exception.ResourceNotFoundException;
import com.rentacar.exception.message.ErrorMessages;
import com.rentacar.repository.RoleRepository;

@Service
public class RoleService {

	private RoleRepository roleRepository;
	@Autowired
	public RoleService(RoleRepository roleRepository) {
		super();
		this.roleRepository = roleRepository;
	}
	
	public Role findByType(RoleType roleType) {
	Role role=roleRepository.findByType(roleType).orElseThrow(()->
		new ResourceNotFoundException(String.format(ErrorMessages.ROLE_NOT_FOUND,roleType.name())));
	return role;
	}
	
	
}

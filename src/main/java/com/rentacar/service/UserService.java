package com.rentacar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentacar.domain.User;
import com.rentacar.exception.ResourceNotFoundException;
import com.rentacar.exception.message.ErrorMessages;
import com.rentacar.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	
	private RoleService roleService;
    @Autowired
	public UserService(UserRepository userRepository, RoleService roleService) {
		super();
		this.userRepository = userRepository;
		this.roleService = roleService;
	}
	
	public User getUserByEmail(String email) {
	User user=userRepository.findByEmail(email).orElseThrow(()->
		new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, email)));
	return user;
	}
	
	
	
}

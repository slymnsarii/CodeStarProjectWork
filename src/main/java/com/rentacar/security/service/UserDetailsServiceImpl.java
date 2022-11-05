package com.rentacar.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rentacar.domain.User;
import com.rentacar.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private UserService userService;
	
	@Autowired
	public UserDetailsServiceImpl(UserService userService) {
		super();
		this.userService = userService;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userService.getUserByEmail(username);
		return UserDetailsImpl.build(user);
	}


}

package com.rentacar.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rentacar.dto.request.LoginRequest;
import com.rentacar.dto.request.RegisterRequest;
import com.rentacar.dto.response.LoginResponse;
import com.rentacar.dto.response.ResponseMessage;
import com.rentacar.dto.response.VRResponse;
import com.rentacar.security.jwt.JwtUtils;
import com.rentacar.service.UserService;

@RestController
public class UserJwtController {
	
	private UserService userService;
	
	private AuthenticationManager authenticationManager;
	
	private JwtUtils jwtUtils;
	
	@Autowired
	public UserJwtController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
		super();
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
	}
 
	// register
	@PostMapping("/register")
	public ResponseEntity<VRResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		userService.saveUser(registerRequest);
		VRResponse response = new VRResponse(ResponseMessage.REGISTER_RESPONSE_MESSAGE, true);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// login
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginRequest.getEmail(), loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String jwtToken = jwtUtils.generateToken (userDetails);
		LoginResponse loginResponse = new LoginResponse(jwtToken);
		return new ResponseEntity<>(loginResponse,HttpStatus.OK); 
	}

}

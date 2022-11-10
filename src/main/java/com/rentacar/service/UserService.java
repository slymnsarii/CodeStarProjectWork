package com.rentacar.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.rentacar.domain.Role;
import com.rentacar.domain.User;
import com.rentacar.domain.enums.RoleType;
import com.rentacar.dto.UserDTO;
import com.rentacar.dto.request.AdminPasswordRequest;
import com.rentacar.dto.request.AdminUserUpdateRequest;
import com.rentacar.dto.request.RegisterRequest;
import com.rentacar.dto.request.UserRequest;
import com.rentacar.exception.BadRequestException;
import com.rentacar.exception.ConflictException;
import com.rentacar.exception.ResourceNotFoundException;
import com.rentacar.exception.message.ErrorMessages;
import com.rentacar.mapper.UserMapper;
import com.rentacar.repository.UserRepository;
import com.rentacar.security.SecurityUtils;

@Service
public class UserService {

	private UserRepository userRepository;

	private RoleService roleService;

	private PasswordEncoder passwordEncoder;

	private UserMapper userMapper;

	@Autowired
	public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder,
			UserMapper userMapper) {
		super();
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
	}

	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, email)));
		return user;
	}

	public void saveUser(RegisterRequest registerRequest) {
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessages.EMAIL_ALREADY_EXISTS_MESSAGE, registerRequest.getEmail()));
		}

		Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		String passwordEncode = passwordEncoder.encode(registerRequest.getPassword());
		User user = new User();
		user.setAddress(registerRequest.getAddress());
		user.setEmail(registerRequest.getEmail());
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setPassword(passwordEncode);
		user.setPhoneNumber(registerRequest.getPhoneNumber());
		user.setZipcode(registerRequest.getZipcode());
		user.setRoles(roles);

		userRepository.save(user);
	}

	public List<UserDTO> getAllUsers() {
		List<User> userList = userRepository.findAll();
		List<UserDTO> userDTOList = userMapper.map(userList);
		return userDTOList;
	}

	public UserDTO getCurrentlyUser() {
		User user = currentUser();
		UserDTO userDTO = userMapper.userToUserDTO(user);
		return userDTO;
	}

	public User currentUser() {
		String email = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_));
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, email)));
		return user;

	}

	public Page<UserDTO> getUserPage(Pageable pageable) {
		Page<User> pageUser = userRepository.findAll(pageable);
		Page<UserDTO> pageUserDTO = pageUser.map(userMapper::userToUserDTO);
		return pageUserDTO;
	}

	public UserDTO getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE, id)));
		UserDTO userDTO = userMapper.userToUserDTO(user);
		return userDTO;
	}

	public void updateUser(Long id, UserRequest userRequest) {
		User user = currentUser();

		buildInException(user);

		if (userRepository.existsByEmail(userRequest.getEmail()) && !userRequest.getEmail().equals(user.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessages.EMAIL_ALREADY_EXISTS_MESSAGE, userRequest.getEmail()));
		}

		String encodePassword = passwordEncoder.encode(userRequest.getPassword());

		user.setAddress(userRequest.getAddress());
		user.setEmail(userRequest.getEmail());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setPassword(encodePassword);
		user.setPhoneNumber(userRequest.getPhoneNumber());
		user.setZipcode(userRequest.getZipcode());

		userRepository.save(user);

	}

	public void buildInException(User user) {

		if (user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessages.BAD_REQUEST_EXCEPTION);
		}

	}

	public void updatePasswordByAdmin(Long id, AdminPasswordRequest adminPasswordRequest) {
		User user = getById(id);

		buildInException(user);

		if (!passwordEncoder.matches(adminPasswordRequest.getOldPassword(), user.getPassword())) {
			throw new BadRequestException(ErrorMessages.PASSWORD_MATCHES_EXCEPTION);
		}
		String newEncodePassword = passwordEncoder.encode(adminPasswordRequest.getNewPassword());
		user.setPassword(newEncodePassword);
		userRepository.save(user);

	}

	private User getById(Long id) {
		return userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE, id)));
	}

	public void deleteUser(Long id) {
		User user = getById(id);
		buildInException(user);
		userRepository.delete(user);
	}

	public void updateUserByAdmin(Long id, @Valid AdminUserUpdateRequest adminUserUpdateRequest) {
		User user = getById(id);
		buildInException(user);

		if (userRepository.existsByEmail(adminUserUpdateRequest.getEmail())
				&& !adminUserUpdateRequest.getEmail().equals(user.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessages.EMAIL_ALREADY_EXISTS_MESSAGE, adminUserUpdateRequest.getEmail()));
		}

		if (adminUserUpdateRequest.getPassword() == null) {
			adminUserUpdateRequest.setPassword(user.getPassword());
		} else {
			String encodePasword = passwordEncoder.encode(adminUserUpdateRequest.getPassword());
			adminUserUpdateRequest.setPassword(encodePasword);
		}
		Set<String> roles = adminUserUpdateRequest.getRoles();
						
		Set<Role> userRoles = convertRole(roles);

		user.setAddress(adminUserUpdateRequest.getAddress());
		user.setBuiltIn(adminUserUpdateRequest.getBuiltIn());
		user.setEmail(adminUserUpdateRequest.getEmail());
		user.setFirstName(adminUserUpdateRequest.getFirstName());
		user.setLastName(adminUserUpdateRequest.getLastName());
		user.setPassword(adminUserUpdateRequest.getPassword());
		user.setPhoneNumber(adminUserUpdateRequest.getPhoneNumber());
		user.setRoles(userRoles);
		user.setZipcode(adminUserUpdateRequest.getZipcode());
		
		userRepository.save(user);

	}

	public Set<Role> convertRole(Set<String> roles) {
		Set<Role> userRoles = new HashSet<>();

		if (roles == null) {
			Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
			userRoles.add(role);
		} else {
			roles.forEach(r -> {
				if (r.equals(RoleType.ROLE_ADMIN.getName())) {
					Role role = roleService.findByType(RoleType.ROLE_ADMIN);
					userRoles.add(role);
				} else {
					Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
					userRoles.add(role);
					
				}
			});
		}
		return userRoles;
	}

}

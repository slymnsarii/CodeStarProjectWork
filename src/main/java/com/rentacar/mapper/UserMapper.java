package com.rentacar.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.rentacar.domain.ImageFile;
import com.rentacar.domain.User;
import com.rentacar.dto.ImageFileDTO;
import com.rentacar.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

	
	UserDTO userToUserDTO(User user);
	
	List<UserDTO> map(List<User> userList);
	
	
	
}

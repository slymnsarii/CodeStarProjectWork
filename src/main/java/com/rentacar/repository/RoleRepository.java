package com.rentacar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentacar.domain.Role;
import com.rentacar.domain.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

	Optional<Role> findByType(RoleType type);
	// Derived methods
}

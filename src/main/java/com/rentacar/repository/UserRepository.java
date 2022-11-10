package com.rentacar.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentacar.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	
	Boolean existsByEmail(String email);
	
	@EntityGraph(attributePaths = "roles")
	Optional<User> findByEmail(String email);
 	
	@EntityGraph(attributePaths = "roles")
	Page<User> findAll(Pageable pageable);
	
	@EntityGraph(attributePaths = "roles")
	Optional<User> findById(Long id);
	
	
}

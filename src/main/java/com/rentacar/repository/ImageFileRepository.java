package com.rentacar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentacar.domain.ImageFile;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, String> {

	
	@EntityGraph("id")
	List<ImageFile> findAll();

}

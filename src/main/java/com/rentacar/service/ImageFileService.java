package com.rentacar.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rentacar.domain.ImageData;
import com.rentacar.domain.ImageFile;
import com.rentacar.dto.ImageFileDTO;
import com.rentacar.exception.ResourceNotFoundException;
import com.rentacar.exception.message.ErrorMessages;
import com.rentacar.repository.ImageFileRepository;



@Service
public class ImageFileService {

	private ImageFileRepository imageFileRepository;
	@Autowired
	public ImageFileService(ImageFileRepository imageFileRepository) {
		super();
		this.imageFileRepository = imageFileRepository;
	}
	public String saveImage(MultipartFile multipartFile) {
		
		ImageFile imageFile=null;
		
		String fileName=StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
		
		
		try {
			ImageData imageData=new ImageData(multipartFile.getBytes());
			imageFile=new ImageFile(fileName,multipartFile.getContentType(),imageData);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		imageFileRepository.save(imageFile);
		
		return imageFile.getId();
		
	}
	public ImageFile getImageById(String id) {
		ImageFile imageFile=imageFileRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException(String.format(ErrorMessages.EMAIL_ALREADY_EXISTS_MESSAGE, id)));
		return imageFile; 
	}
	public List<ImageFileDTO> getAllImages() {
		
		 List<ImageFile> imageFileList=imageFileRepository.findAll();
		 
		 List<ImageFileDTO> imageFileDTOs=imageFileList.stream().map(imgFile->
		 {
			 String imageUri=ServletUriComponentsBuilder.
					 fromCurrentContextPath().
					 path("/files/download/").
					 path(imgFile.getId()).toUriString();
			 
			 return new ImageFileDTO(imgFile.getName(), imageUri,imgFile.getType(),imgFile.getLength());
			 
			 
		 }).collect(Collectors.toList());
		 
		 return imageFileDTOs;
	}
	public void deleteImageById(String id) {
		ImageFile imageFile=getImageById(id);
		imageFileRepository.delete(imageFile);		
	}
	
	
	
	
	
}

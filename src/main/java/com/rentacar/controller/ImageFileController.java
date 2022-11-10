package com.rentacar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentacar.domain.ImageFile;
import com.rentacar.dto.ImageFileDTO;
import com.rentacar.dto.response.ImageSaveResponse;
import com.rentacar.dto.response.ResponseMessage;
import com.rentacar.dto.response.VRResponse;
import com.rentacar.service.ImageFileService;



@RestController
@RequestMapping("/files")
public class ImageFileController {

	private ImageFileService imageFileService;
	@Autowired
	public ImageFileController(ImageFileService imageFileService) {
		super();
		this.imageFileService = imageFileService;
	}
	
	// upload image
	@PostMapping("/upload")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ImageSaveResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile ){
	String imageId=imageFileService.saveImage(multipartFile);
		ImageSaveResponse imageSaveResponse=new ImageSaveResponse(imageId,ResponseMessage.IMAGE_SAVE_RESPONSE,true);
	return ResponseEntity.ok(imageSaveResponse);
	}
	
	// download
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String id){
		
		ImageFile imageFile=imageFileService.getImageById(id);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+imageFile.getName()).
				body(imageFile.getImageData().getData());
		
	}
	
	// display
	
	//uuid=univarsal unique id
	
	@GetMapping("/display/{id}")
	public ResponseEntity<byte[]> displayFile(@PathVariable("id") String id){
		
		ImageFile imageFile=imageFileService.getImageById(id);
		
		HttpHeaders headers=new HttpHeaders();
		
		headers.setContentType(MediaType.IMAGE_PNG);
		
		return new ResponseEntity<>(imageFile.getImageData().getData(),headers,HttpStatus.OK);
		
	}
	
	// get all images
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ImageFileDTO>> getAllImages(){
		
	List<ImageFileDTO> imageFileDtos=imageFileService.getAllImages();
	
	return ResponseEntity.ok(imageFileDtos);
	}
	
	// delete image
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> deleteImageFile(@PathVariable String id){
		imageFileService.deleteImageById(id);
		VRResponse response=new VRResponse(ResponseMessage.IMAGE_DELETE_RESPONSE,true);
		return ResponseEntity.ok(response);
	}
	
	
	
	
}

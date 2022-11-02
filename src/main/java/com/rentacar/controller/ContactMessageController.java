package com.rentacar.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rentacar.domain.ContactMessage;
import com.rentacar.dto.ContactMessageDTO;
import com.rentacar.dto.request.ContactMessageRequest;
import com.rentacar.dto.response.VRResponse;
import com.rentacar.mapper.ContactMessageMapper;
import com.rentacar.service.ContactMessageService;

@RestController
@RequestMapping("/contactmessage")
public class ContactMessageController {
	
	 private ContactMessageService contactMessageService;
	 private ContactMessageMapper contactMessageMapper;

	 @Autowired
	 public ContactMessageController(ContactMessageService contactMessageService,
			ContactMessageMapper contactMessageMapper) {
		
		this.contactMessageService = contactMessageService;
		this.contactMessageMapper = contactMessageMapper;
	}
	 @PostMapping("/ziyaretci")
	 public ResponseEntity<VRResponse> creatContactMessages(@Valid @RequestBody
			 ContactMessageRequest contactMessageRequest  ){
		 
		 ContactMessage contactMessage= contactMessageMapper.
				 contactMessageRequestToContactMessage(contactMessageRequest);
		 contactMessageService.saveMessage(contactMessage);
		 
		 VRResponse vrResponse=new VRResponse("Mesajiniz basariyla olustu",true);
		 
		 return new ResponseEntity<>(vrResponse,HttpStatus.CREATED);
		 
	 }
	 @GetMapping
	 public ResponseEntity<List<ContactMessageDTO>> getAllContactMessages(){
		 
		 List<ContactMessage> contactMessagesList=contactMessageService.getAllMessages();
		//mapleme yapacagiz
		 List<ContactMessageDTO>contactMessageDTOList=
				 contactMessageMapper.map(contactMessagesList);
		 return ResponseEntity.ok(contactMessageDTOList);
		
	 }
	 
	 @GetMapping("/page")
	 
	 public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessagesWithPageSystem
	 
	 		(@RequestParam("page")int page,
			 @RequestParam("size")int size,
			 @RequestParam("sort")String prop,
			 @RequestParam(value = "direction", required = false,
			 defaultValue = "DESC")Direction direction){
		 Pageable pageable=PageRequest.of(page, size,Sort.by(direction,prop));
		 
		 Page<ContactMessage> contactmessagePage= contactMessageService.getAllMessages(pageable);
		 
		Page<ContactMessageDTO> pageDto=getPageDTO(contactmessagePage);
		return ResponseEntity.ok(pageDto);
	 }
	 
	 private Page<ContactMessageDTO> getPageDTO(Page<ContactMessage> contactmessagePage){
		 Page<ContactMessageDTO> dtoPage= 
		contactmessagePage.map(new java.util.function.Function<ContactMessage,ContactMessageDTO>(){
			
			
			public ContactMessageDTO apply(ContactMessage contactMessage) {
				return contactMessageMapper.contactMessageToDTO(contactMessage);
			}
			 
		 });
		 return dtoPage;
	 }
	 


}

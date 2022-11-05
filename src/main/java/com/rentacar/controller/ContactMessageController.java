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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rentacar.domain.ContactMessage;
import com.rentacar.dto.ContactMessageDTO;
import com.rentacar.dto.request.ContactMessageRequest;
import com.rentacar.dto.response.ResponseMessage;
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
	public ResponseEntity<VRResponse> createContactMessages(
			@Valid @RequestBody ContactMessageRequest contactMessageRequest) {
		ContactMessage contactMessage = contactMessageMapper
				.contactMessageRequestToContactMessage(contactMessageRequest);
		contactMessageService.saveMessage(contactMessage);
		VRResponse response = new VRResponse(ResponseMessage.CONTACTMESSAGE_CREATE_RESPONSE, true);
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@GetMapping
	public ResponseEntity<List<ContactMessageDTO>> getAllContactMessages() {
		List<ContactMessage> contactMessagesList = contactMessageService.getAllMessages();
		List<ContactMessageDTO> contactMessageDTOList = contactMessageMapper.map(contactMessagesList);

		return ResponseEntity.ok(contactMessageDTOList);
	}

	@GetMapping("/page")
	public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessagesWithPaging(@Valid
																		@RequestParam("page")int page,
																		@RequestParam("size")int size,
																		@RequestParam("sort")String prop,
																		@RequestParam(value = "direction",
																		required = false,defaultValue = "DESC")Direction direction){
		
		Pageable pageable=PageRequest.of(page, size,Sort.by(direction,prop));
		Page<ContactMessage> contactMessagePage=contactMessageService.getAllMessages(pageable);
		
		Page<ContactMessageDTO> pageDTOPage=getPageDTO(contactMessagePage);
		
		return ResponseEntity.ok(pageDTOPage);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ContactMessageDTO> getContactMessageByIdWithPath(@PathVariable("id")Long id){
	ContactMessage contactMessage=contactMessageService.getMessageById(id);
	ContactMessageDTO contactMessageDTO=contactMessageMapper.contactMessageToDTO(contactMessage);
	return ResponseEntity.ok(contactMessageDTO);
	} 
	
	@GetMapping("/request")
	public ResponseEntity<ContactMessageDTO> getContactMessageByIdWithParam(@RequestParam("id")Long id){
	ContactMessage contactMessage=contactMessageService.getMessageById(id);
	ContactMessageDTO contactMessageDTO=contactMessageMapper.contactMessageToDTO(contactMessage);
	return ResponseEntity.ok(contactMessageDTO);
	} 
	
	@DeleteMapping("/{id}")
	public ResponseEntity<VRResponse> deleteContactMessage(@PathVariable("id")Long id){
		contactMessageService.deleteContactMessage(id);
		VRResponse response=new VRResponse(ResponseMessage.CONTACTMESSAGE_DELETE_RESPONSE,true);
	return ResponseEntity.ok(response);	
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<VRResponse> updateContactMessage(@PathVariable("id")Long id,@Valid @RequestBody ContactMessageRequest contactMessageRequest){
	ContactMessage contactMessage=contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
		contactMessageService.updateContactMessage(id,contactMessage);
		VRResponse response=new VRResponse(ResponseMessage.CONTACTMESSAGE_UPDATE_RESPONSE,true);
		
		return ResponseEntity.ok(response);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private Page<ContactMessageDTO> getPageDTO(Page<ContactMessage> contactMessagePage){
		
		Page<ContactMessageDTO> dtoPage=contactMessagePage.
				map(new java.util.function.Function<ContactMessage,ContactMessageDTO>(){
					@Override
					public ContactMessageDTO apply(ContactMessage contactMessage) {
						
						return contactMessageMapper.contactMessageToDTO(contactMessage);
					}
				});
		
		return dtoPage;
				
	}
	
	
	
	
	
	
}

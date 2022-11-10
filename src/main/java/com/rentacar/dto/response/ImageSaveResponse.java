package com.rentacar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSaveResponse extends VRResponse{

	private  String id;

	public ImageSaveResponse(String id,String message, Boolean success ) {
		super(message, success);
		this.id = id;
	}
	
	
}

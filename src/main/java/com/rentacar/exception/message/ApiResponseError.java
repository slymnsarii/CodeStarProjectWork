package com.rentacar.exception.message;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponseError {
	
	private HttpStatus status;
	
	private String message;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss")
	@Setter(AccessLevel.NONE)// bu variablenin setter methodu kullanılmasın
	private LocalDateTime timeStamp;
	
	private String requestURI;

	private ApiResponseError() {
		timeStamp=LocalDateTime.now();
	}
	public ApiResponseError(HttpStatus status) {
		this();
		this.status=status;
		this.message="Unexpected Error";
	}
	public ApiResponseError(HttpStatus status,String message,String requestURI) {
		this(status);
		this.message=message;
		this.requestURI=requestURI;
	}
	
	
}

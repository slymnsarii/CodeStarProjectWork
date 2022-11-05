package com.rentacar.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rentacar.exception.message.ApiResponseError;



@ControllerAdvice
public class RentACarExceptionHandler extends ResponseEntityExceptionHandler {
	
	Logger logger=LoggerFactory.getLogger(RentACarExceptionHandler.class);

	private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
		logger.error(error.getMessage());
		return new ResponseEntity<>(error, error.getStatus());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ApiResponseError error = new ApiResponseError(HttpStatus.NOT_FOUND, ex.getMessage(),
				request.getDescription(false));
		return buildResponseEntity(error);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(t -> t.getDefaultMessage())
				.collect(Collectors.toList());

		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, errors.get(0).toString(),
				request.getDescription(false));

		return buildResponseEntity(error);

	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(),
				request.getDescription(false));

		return buildResponseEntity(error);
	}

	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
				request.getDescription(false));

		return buildResponseEntity(error);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(),
				request.getDescription(false));

		return buildResponseEntity(error);
	}
	
	
	
	
	
	
	
	
	
	
	
	

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<Object> handleRunTimeException(RuntimeException ex, WebRequest request) {
		ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
				request.getDescription(false));
		return buildResponseEntity(error);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
		ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
				request.getDescription(false));
		return buildResponseEntity(error);
	}

}

package com.easyBank.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.easyBank.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomerAlreadyException.class)
	public ResponseEntity<ErrorResponseDto>handleGlobalExceptionForCustomerAlreadyDefined(CustomerAlreadyException exception,WebRequest webRequest){
		
		ErrorResponseDto errorResponseDto=new ErrorResponseDto(webRequest.getDescription(false),
				HttpStatus.BAD_REQUEST, 
				exception.getMessage(), 
				LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponseDto>(errorResponseDto, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)

	
public ResponseEntity<ErrorResponseDto>handleGlobalExceptionForResourceNotFoundException(ResourceNotFoundException exception,WebRequest webRequest){
		
		ErrorResponseDto errorResponseDto=new ErrorResponseDto(webRequest.getDescription(false),
				HttpStatus.NOT_FOUND, 
				exception.getMessage(), 
				LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponseDto>(errorResponseDto, HttpStatus.NOT_FOUND);
	}
}
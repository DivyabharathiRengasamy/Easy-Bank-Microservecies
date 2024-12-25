package com.easyBank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerAlreadyException extends RuntimeException {

	
	public CustomerAlreadyException(String message) {
		 super(message);
	}
}

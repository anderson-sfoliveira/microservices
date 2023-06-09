package com.microservices.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2603138221907861192L;

	public ResourceNotFoundException(String exception) {
		super(exception);
	}
}

package com.coindata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler extends Exception {

	private static final long serialVersionUID = 1L;

	@ExceptionHandler(InvalidUsernameException.class)
	public ResponseEntity<String> invalidUsername(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidEmailException.class)
	public ResponseEntity<String> invalidEmail(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> userNotFound(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
}
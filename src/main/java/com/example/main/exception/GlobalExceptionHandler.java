package com.example.main.exception;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	

	@ExceptionHandler(InvalidIdException.class)
	public ResponseEntity<?> handleInvalidIdException(InvalidIdException idException, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), idException.getMessage(),
				webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}

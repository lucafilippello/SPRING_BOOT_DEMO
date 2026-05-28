package it.technologydata.tech4tech.FullRestApp.advice;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import it.technologydata.tech4tech.FullRestApp.exception.DuplicateUsernameException;
import it.technologydata.tech4tech.FullRestApp.exception.MyNotFoundException;
import it.technologydata.tech4tech.FullRestApp.validation.CustomError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;


@RestControllerAdvice
public class MyResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	//Gestione eccezioni custom
    @ExceptionHandler(MyNotFoundException.class)
	protected ResponseEntity<Object> handleMyNotFoundException(MyNotFoundException ex) {
    	
    	CustomError error = CustomError.builder()
    	.timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()))
    	.httpStatus(HttpStatus.NO_CONTENT.value())
    	.httpStatusCode(HttpStatus.NO_CONTENT)
    	.errorMessage(ex.getMessage())
    	.build();
		return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(error);
	}
    
    @ExceptionHandler(DuplicateUsernameException.class)
	protected ResponseEntity<Object> handleDupliactedUsername(DuplicateUsernameException ex) {
    	
    	CustomError error = CustomError.builder()
    	.timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()))
    	.httpStatus(HttpStatus.CONFLICT.value())
    	.httpStatusCode(HttpStatus.CONFLICT)
    	.errorMessage(ex.getMessage())
    	.build();
		return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(error);
	}
    
    @ExceptionHandler(HttpClientErrorException.class)
	protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
		return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getResponseBodyAsString());
	}

    //Gestione eccezioni lanciate a fronte di errori di validazione
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    	
    	List<String> errors = new ArrayList<String>();
    	
    	final String fieldError = "Field '%s' has invalid value '%s'. %s";
    	//final StringBuffer sb = new StringBuffer();
    	ex.getBindingResult().getFieldErrors().stream().forEach(err -> {
    		//sb.append(String.format(fieldError,err.getField(),err.getRejectedValue(),err.getDefaultMessage()));
    		errors.add(String.format(fieldError,err.getField(),err.getRejectedValue(),err.getDefaultMessage()));
    		//logger.info(sb.append(String.format(fieldError,err.getField(),err.getRejectedValue(),err.getDefaultMessage())).toString());
    	});
    	
    	ex.getBindingResult().getGlobalErrors().stream().forEach( error -> {
    		errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    	});

    	String errorMessage = "Invalid request";
    	
    	CustomError error = CustomError.builder()
    			.timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()))
    	    	.httpStatus(HttpStatus.BAD_REQUEST.value())
    	    	.httpStatusCode(HttpStatus.BAD_REQUEST)
    	    	.errorMessage(errorMessage)
    	    	.errors(errors)
    	    	.build();
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
	    List<String> errors = new ArrayList<String>();
	    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
	        errors.add(violation.getRootBeanClass().getName() + " " +  violation.getPropertyPath() + ": " + violation.getMessage());
	    }
	    
	    CustomError apiError = CustomError.builder()
	    		.timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()))
	    		.httpStatus(HttpStatus.BAD_REQUEST.value())
	    		.httpStatusCode(HttpStatus.BAD_REQUEST)
	    		.errorMessage("Invalid request due to type constraints violations in request body AND (OR) query (OR path) parameters")
	    		.errors(errors).build();
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
	}
	
	
	
	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		//logger.info("NO RESOURCE FOUND:"+ex.getBody().getDetail());
		CustomError apiError = CustomError.builder()
				.timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()))
	    		.httpStatus(HttpStatus.NOT_FOUND.value())
	    		.httpStatusCode(HttpStatus.NOT_FOUND)
	    		.errorMessage("Resource "+ex.getResourcePath()+" not found")
	    		.build();
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
	}
}

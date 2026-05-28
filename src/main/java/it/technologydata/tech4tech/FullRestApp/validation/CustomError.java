package it.technologydata.tech4tech.FullRestApp.validation;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomError {
	
	private String timestamp;
	private int httpStatus;
	private HttpStatus httpStatusCode;
	private String path;
	private String errorMessage;
	private List<String> errors;
}

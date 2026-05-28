package it.technologydata.tech4tech.FullRestApp.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.technologydata.tech4tech.FullRestApp.service.LoggingService;
import it.technologydata.tech4tech.FullRestApp.validation.CustomError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private LoggingService loggingService;
	
	private static final String ERR_MSG = " - Access to resource '[%s] %s'  denied to user '%s'";

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
		String httpMethod = request.getMethod();
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter writer = response.getWriter();

        String responseBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
        		CustomError.builder()
        		.timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()))
        		.errorMessage(accessDeniedException.getMessage() + String.format(ERR_MSG, httpMethod, request.getRequestURI(), loggedUser ))
        		.httpStatus(HttpStatus.FORBIDDEN.value())
        		.httpStatusCode(HttpStatus.FORBIDDEN).build());
        
        loggingService.logRespMetadata(response);
        loggingService.logBody("Response", responseBody);
        
        writer.println(responseBody);
		
	}

}

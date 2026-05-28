package it.technologydata.tech4tech.FullRestApp.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.technologydata.tech4tech.FullRestApp.service.LoggingService;
import it.technologydata.tech4tech.FullRestApp.validation.CustomError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private LoggingService loggingService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException {
    	
    	String requestBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    	loggingService.logReqMetadata(request);
        loggingService.logBody("Request", requestBody);
    	//logger.info(">>>> COMMENCE:"+authEx.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();

        String responseBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
        		CustomError.builder()
        		.timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()))
        		.errorMessage(authEx.getMessage())
        		.httpStatus(HttpStatus.UNAUTHORIZED.value())
        		.httpStatusCode(HttpStatus.UNAUTHORIZED).build());
        
        loggingService.logRespMetadata(response);
        loggingService.logBody("Response", responseBody);
        
        writer.println(responseBody);
    }
    
    @Override
    public void afterPropertiesSet() {
        setRealmName("rest-demo-app");
        super.afterPropertiesSet();
    }
}

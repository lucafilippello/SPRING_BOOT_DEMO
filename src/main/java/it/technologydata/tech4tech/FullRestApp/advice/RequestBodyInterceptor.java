package it.technologydata.tech4tech.FullRestApp.advice;

import java.lang.reflect.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.technologydata.tech4tech.FullRestApp.service.LoggingService;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RequestBodyInterceptor extends RequestBodyAdviceAdapter {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Value("${rest.log.pretty.print}")
	private boolean prettyPrint;

	@Autowired
    LoggingService loggingService;

    @Autowired
    HttpServletRequest request;
    
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

		String rawBody = body.toString();
		try {
			if(prettyPrint) {
				rawBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
			} else {
				rawBody = mapper.writeValueAsString(body);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		loggingService.logBody("Request", rawBody);
		return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
	}
}

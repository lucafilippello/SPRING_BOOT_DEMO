package it.technologydata.tech4tech.FullRestApp.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.technologydata.tech4tech.FullRestApp.service.LoggingService;

@ControllerAdvice
public class ResponseBodyInterceptor implements ResponseBodyAdvice<Object> {
	

	@Autowired
	private ObjectMapper mapper;
	
	@Value("${rest.log.pretty.print}")
	private boolean prettyPrint;
	
	@Autowired
    private LoggingService loggingService;
	

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if(body != null) {
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
			loggingService.logBody("Response",rawBody);
		}
		
        return body;
	}
}

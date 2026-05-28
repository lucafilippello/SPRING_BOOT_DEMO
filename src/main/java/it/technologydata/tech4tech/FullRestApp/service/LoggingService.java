package it.technologydata.tech4tech.FullRestApp.service;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LoggingService {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

	public void logReqMetadata(HttpServletRequest request) {

		StringBuilder requestLog = new StringBuilder("\n###### START REQUEST ######");
		requestLog.append("\n"+request.getMethod()+ " "+request.getRequestURI()+" "+request.getProtocol());
		Map<String,String> headers = getRequestHeaders(request);
		
		if(!headers.isEmpty()) {
			requestLog.append("\nHTTP Headers:");
			headers.keySet().stream().forEach(headerName -> {
				requestLog.append("\n"+headerName+": "+headers.get(headerName));
			});
		}
		
		Map<String,String> parameters = getRequestParameters(request);
		if(!parameters.isEmpty()) {
			requestLog.append("\nHTTP Request parameters:");
			parameters.keySet().stream().forEach(parameterName -> {
				requestLog.append("\n"+parameterName+": "+parameters.get(parameterName));
			});
		}

		requestLog.append("\n###### END REQUEST ######");
		
		logger.info(requestLog.toString());
	}

	public void logRespMetadata(HttpServletResponse response) {
		// TODO Auto-generated method stub
		StringBuilder responseLog = new StringBuilder("\n###### START RESPONSE ######");
		responseLog.append("\nHTTP status code: "+response.getStatus());
		Map<String,String> headers = getResponseHeaders(response);
		if(!headers.isEmpty()) {
			responseLog.append("\nHTTP Headers:");
			headers.keySet().stream().forEach(headerName -> {
				responseLog.append("\n"+headerName+": "+headers.get(headerName));
			});
		}

		responseLog.append("\n###### END RESPONSE ######");
		
		logger.info(responseLog.toString());

	}
	
	private Map<String,String> getRequestHeaders(HttpServletRequest request) {
		
		Map<String,String> headersMap = new HashMap<>();
		Enumeration<String> httpHeaders = request.getHeaderNames();
		while (httpHeaders.hasMoreElements()) {
			String headerName = httpHeaders.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);
			StringBuffer headerValuesStr = new StringBuffer();
			while(headerValues.hasMoreElements()) {
				headerValuesStr.append(","+headerValues.nextElement());
			}
			headersMap.put(headerName, headerValuesStr.substring(1));
		}
		
		return headersMap;
	}
	
	private Map<String,String> getResponseHeaders(HttpServletResponse response) {
		
		Map<String,String> headersMap = new HashMap<>();
		Collection<String> httpHeaders = response.getHeaderNames();
		for (String headerName : httpHeaders) {
			Collection<String> headerValues = response.getHeaders(headerName);
			StringBuffer headerValuesStr = new StringBuffer();
			for(String headerValue : headerValues) {
				headerValuesStr.append(","+headerValue);
			}
			headersMap.put(headerName, headerValuesStr.substring(1));
		}
		
		return headersMap;
	}
	
	private Map<String,String> getRequestParameters(HttpServletRequest request) {
		
		Map<String,String> parametersMap = new HashMap<>();
		Map<String,String[]> _parametersMap = request.getParameterMap();
		_parametersMap.keySet().stream().forEach(key -> {
			StringBuffer pv = new StringBuffer();
			String[] paramValues = _parametersMap.get(key);
			for (String paramValue : paramValues) {
				pv.append(","+paramValue);
			}
			parametersMap.put(key, pv.substring(1));
		});
		
		return parametersMap;
	}

	public void logBody(String object, String body) {
		if(!StringUtils.isBlank(body)) {
			logger.info(String.format("\n###### START %s BODY ######\n %s \n###### END %s BODY ######", object.toUpperCase(),body,object.toUpperCase()));
		}
	}

}

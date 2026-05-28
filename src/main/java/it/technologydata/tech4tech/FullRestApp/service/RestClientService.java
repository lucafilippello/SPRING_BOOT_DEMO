package it.technologydata.tech4tech.FullRestApp.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.technologydata.tech4tech.FullRestApp.dto.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestClientService {
	
	@Autowired
	private ObjectMapper mapper;

	private RestClient restClient;
	
	private void setRestClient(final String authParam) {
		String username = null;
		String password = null;
		
		switch (authParam) {
			case "HR" : {
				username = "hr1";
				password = "@hr1@";
				break;
			}
			case "ADMIN" : {
				username = "admin1";
				password = "#admin1#";
				break;
			}
			case "WRONG" : {
				username = "pippo";
				password = "pluto";
				break;
			}
		}
		
		if(username == null || password == null) {
			this.restClient = RestClient.builder()
		      		  .baseUrl("http://localhost:8080/rest-demo-app/employee")
		      		  .build();
		} else {
			String auth = username + ":" + password;
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8) );
	        String authHeader = "Basic " + new String( encodedAuth );
	        
	        this.restClient = RestClient.builder()
	      		  .baseUrl("http://localhost:8080/rest-demo-app/employee")
	      		  .defaultHeader("Authorization", authHeader)
	      		  .build();
		}
	}

	public ResponseEntity<EmployeeDTO> getEmployeeById(Long id, String authParam) throws JsonProcessingException, HttpClientErrorException {
		
		setRestClient(authParam);
		
		ResponseEntity<String> resp;
		try {
			resp = this.restClient
					.get()
					.uri("/id/{id}", id)
					.retrieve()
					.toEntity(String.class);
		} catch (HttpClientErrorException e) {
			//logger.info("Status code:"+e.getStatusCode());
			//logger.info("Error Body:\n"+e.getResponseBodyAsString());
			throw e;
		}
		
		EmployeeDTO empDTO = mapper.readValue(resp.getBody(), EmployeeDTO.class);
		return ResponseEntity.status(resp.getStatusCode()).body(empDTO);

	}
	
	public ResponseEntity<EmployeeDTO> saveEmployee(EmployeeDTO employeeDTO, String authParam) throws JsonProcessingException, HttpClientErrorException {
		
		setRestClient(authParam);
		
		ResponseEntity<String> resp;
		try {
			resp = this.restClient
				.post()
				//.uri("/")
				.body(employeeDTO)
				.retrieve().toEntity(String.class);
		} catch (HttpClientErrorException e) {
			throw e;
		}
		
		EmployeeDTO empDTO = mapper.readValue(resp.getBody(), EmployeeDTO.class);
		return ResponseEntity.status(resp.getStatusCode().value()).body(empDTO);
	}

}

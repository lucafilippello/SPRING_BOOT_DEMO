package it.technologydata.tech4tech.FullRestApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.technologydata.tech4tech.FullRestApp.dto.EmployeeDTO;
import it.technologydata.tech4tech.FullRestApp.service.RestClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/client")
@Validated
public class ClientController {
	
	@Autowired
	private RestClientService clientService;
	
	// GET /rest-demo-app/client/7
	@GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	/*@Operation(summary = "Get employee by client call")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employees successfully retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)*/
	public ResponseEntity<EmployeeDTO> getEmployeeByClient(@PathVariable @Positive Long id, @RequestParam(required = false, defaultValue = "NO") String auth) throws JsonProcessingException,HttpClientErrorException {
		
		return clientService.getEmployeeById(id,auth);
	}
	
	
	// POST /rest-demo-app/client
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	/*@Operation(summary = "Insert new employee by client call")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully inserted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)*/
	@Transactional
	public ResponseEntity<EmployeeDTO> insertEmployee(@Valid @RequestBody EmployeeDTO employeeDTO, @RequestParam(required = false, defaultValue = "NO") String auth) throws JsonProcessingException, HttpClientErrorException {
		return clientService.saveEmployee(employeeDTO,auth);
	}

}

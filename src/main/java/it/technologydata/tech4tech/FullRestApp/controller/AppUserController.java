package it.technologydata.tech4tech.FullRestApp.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.technologydata.tech4tech.FullRestApp.dto.AppUserDTO;
import it.technologydata.tech4tech.FullRestApp.entity.AppUser;
import it.technologydata.tech4tech.FullRestApp.exception.AppUserNotFoundException;
import it.technologydata.tech4tech.FullRestApp.exception.DuplicateUsernameException;
import it.technologydata.tech4tech.FullRestApp.service.AppUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/user")
@Validated
@SecurityRequirement(name = "rest_demo_app")
public class AppUserController {
	
	@Autowired
	private AppUserService appUserService;
	
	// GET /rest-demo-app/user/all
	@GetMapping(value="/all",produces = MediaType.APPLICATION_JSON_VALUE)
	/*@Operation(summary = "Get all app users")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Users successfully retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)*/
	public List<AppUserDTO> getAllAppUsers() {

		List<AppUser> users = appUserService.getAll();
		//Conversione entity -> dto
		List<AppUserDTO> employeesDTO = users.stream().map(user -> user.convertEntityToDto()).collect(Collectors.toList());
		return employeesDTO;
	}
	
	// GET /rest-demo-app/user/myusername
	@GetMapping(value="/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	/*@Operation(summary = "Get user by username")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "App User successfully retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "204", description = "App User not found"),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)*/
	public ResponseEntity<AppUserDTO> getAppUserByUsername(@PathVariable @NotBlank String username) throws AppUserNotFoundException {
		
		AppUser user = appUserService.getByUsername(username);
		return ResponseEntity.ok(user.convertEntityToDto());
		
	}
	
	// POST /rest-demo-app/user
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	/*@Operation(summary = "Insert new user on db")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "User successfully inserted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "409", description = "Duplicate username found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)*/
	@Transactional
	public AppUserDTO insertUser(@Valid @RequestBody AppUserDTO appUserDTO) throws DuplicateUsernameException {
		return appUserService.saveNew(appUserDTO).convertEntityToDto();
	}
	
	

}

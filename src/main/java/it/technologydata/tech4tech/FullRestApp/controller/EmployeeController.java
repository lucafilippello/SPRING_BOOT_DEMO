package it.technologydata.tech4tech.FullRestApp.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import it.technologydata.tech4tech.FullRestApp.dto.EmployeeDTO;
import it.technologydata.tech4tech.FullRestApp.entity.Employee;
import it.technologydata.tech4tech.FullRestApp.enums.Job;
import it.technologydata.tech4tech.FullRestApp.enums.SalaryCriteria;
import it.technologydata.tech4tech.FullRestApp.enums.TemporalCriteria;
import it.technologydata.tech4tech.FullRestApp.exception.EmployeeNotFoundException;
import it.technologydata.tech4tech.FullRestApp.service.EmployeeService;
import it.technologydata.tech4tech.FullRestApp.validation.CustomDatePattern;
import it.technologydata.tech4tech.FullRestApp.validation.CustomError;
import it.technologydata.tech4tech.FullRestApp.validation.JavaBeanProperty;
import it.technologydata.tech4tech.FullRestApp.validation.ValueOfEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employee")
@Validated
@EnableMethodSecurity
@SecurityRequirement(name = "rest_demo_app")
@Slf4j
public class EmployeeController {
	
	@Autowired
	private EmployeeService emplService;

	
	// GET /rest-demo-app/employee/all?orderBy=XXX&orderDir=YYY
	@GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get all employees")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employees successfully retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public List<EmployeeDTO> getAllEmployees(
			@RequestParam(required = false, defaultValue = "id") @JavaBeanProperty(beanClass = Employee.class, availableValues = "birthDate,fireDate,hireDate,id,job,name,salary,surname") String orderBy,
			@RequestParam(required = false, defaultValue = "ASC") @ValueOfEnum(enumClass = Sort.Direction.class, availableValues = "ASC,DESC")  String orderDir) {
		List<Employee> employees = emplService.getAllEmployees(orderBy,Direction.valueOf(orderDir));
		//Conversione entity -> dto
		List<EmployeeDTO> employeesDTO = employees.stream().map(empl -> empl.convertEntityToDto()).collect(Collectors.toList());
		return employeesDTO;
	}
	
	// GET /rest-demo-app/employee/id/7
	@GetMapping(value="/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get employee by Id")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "204", description = "Employee not found"),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public EmployeeDTO getEmployee(@PathVariable @Positive Integer id) throws EmployeeNotFoundException {
		
		Employee empl  = emplService.getEmployeeById(id);
		//Conversione entity -> dto
		return empl.convertEntityToDto();//ResponseEntity.ok(empl.convertEntityToDto());
		
	}
	
	// GET /rest-demo-app/employee/fullname/tony/stark
	@GetMapping(value="/fullname/{name}/{surname}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get employee by name and surname")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "204", description = "Employee not found"),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public EmployeeDTO getEmployeeByFullName(
			@PathVariable @NotBlank String name,
			@PathVariable @NotBlank String surname
			) throws EmployeeNotFoundException {
		
		Employee employee  = emplService.getEmployeeByFullName(name, surname);
		//Conversione entity -> dto
		return employee.convertEntityToDto();//ResponseEntity.ok(empl.convertEntityToDto());
		
	}
	
	// GET /rest-demo-app/employee/job/DEVELOPER
	@GetMapping(value="/job/{job}",produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get employees by job")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employees successfully retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public List<EmployeeDTO> getEmployeesByJob(@PathVariable @ValueOfEnum(enumClass = Job.class, availableValues = "DEVELOPER,MANAGER,ANALYST") String job) {
		
		List<Employee> employees = emplService.getEmployeesByJob(job);
		//Conversione entity -> dto
		List<EmployeeDTO> employeesDTO = employees.stream().map(empl -> empl.convertEntityToDto()).collect(Collectors.toList());
		return employeesDTO;
	}
	
	// GET /rest-demo-app/employee/birthdate/AFTER/2000-12-01
	@GetMapping(value="/birthdate/{criteria}/{birthdate}",produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get employees by birthdate criteria")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employees successfully counted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public List<EmployeeDTO> getEmployeesByBirthdate(
			@PathVariable @ValueOfEnum(enumClass = TemporalCriteria.class, availableValues = "BEFORE,AFTER") String criteria,
			//@PathVariable @CustomDatePattern(dateFormat = "yyyy-MM-dd") Date birthdate) {
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthdate) {

		List<Employee> employees = null;
		if(criteria.equalsIgnoreCase(TemporalCriteria.BEFORE.name())) {
			employees = emplService.getOldestEmployees(birthdate);
		} else {
			employees = emplService.getYoungestEmployees(birthdate);
		}

		//Conversione entity -> dto
		List<EmployeeDTO> employeesDTO = employees.stream().map(empl -> empl.convertEntityToDto()).collect(Collectors.toList());
		return employeesDTO;
	}
	
	// GET /rest-demo-app/employee/hiredate/BEFORE/2000-12-01
	@GetMapping(value="/hiredate/{criteria}/{hiredate}",produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get employees by hiredate criteria")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employees successfully counted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public List<EmployeeDTO> getEmployeesByHiredate(
			@PathVariable @ValueOfEnum(enumClass = TemporalCriteria.class, availableValues = "BEFORE,AFTER") String criteria,
			//@PathVariable @CustomDatePattern(dateFormat = "yyyy-MM-dd") Date hiredate) {
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date hiredate) {
		List<Employee> employees = null;
		if(criteria.equalsIgnoreCase(TemporalCriteria.BEFORE.name())) {
			employees = emplService.getEmployeesWithHighestSeniority(hiredate);
		} else {
			employees = emplService.getEmployeesWithLowestSeniority(hiredate);
		}

		//Conversione entity -> dto
		List<EmployeeDTO> employeesDTO = employees.stream().map(empl -> empl.convertEntityToDto()).collect(Collectors.toList());
		return employeesDTO;
	}
	
	// GET /rest-demo-app/employee/salary/OVER/45000
	@GetMapping(value="/salary/{criteria}/{salary}",produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get employees by salary criteria")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employees successfully counted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public List<EmployeeDTO> getEmployeesBySalary(
			@PathVariable @ValueOfEnum(enumClass = SalaryCriteria.class, availableValues = "UNDER,OVER") String criteria,
			@PathVariable @Positive Double salary) {

		List<Employee> employees = null;
		if(criteria.equalsIgnoreCase(SalaryCriteria.OVER.name())) {
			employees = emplService.getSalaryOverThresholdEmployees(salary);
		} else {
			employees = emplService.getSalaryUnderThresholdEmployees(salary);
		}

		//Conversione entity -> dto
		List<EmployeeDTO> employeesDTO = employees.stream().map(empl -> empl.convertEntityToDto()).collect(Collectors.toList());
		return employeesDTO;
	}
	
	// GET /rest-demo-app/employee/count/job/MANAGER
	@GetMapping(value="/count/job/{job}")
	@Operation(summary = "Count employees by job")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employees successfully counted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public ResponseEntity<Integer> getEmployeesCountByCriteria(@PathVariable @ValueOfEnum(enumClass = Job.class, availableValues = "DEVELOPER,MANAGER,ANALYST") String job) {
		return ResponseEntity.ok(emplService.getCountByJob(job));
	}
	
	// GET /rest-demo-app/employee/count
	@GetMapping(value="/count")
	@Operation(summary = "Count all employees")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employees successfully counted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	public ResponseEntity<Integer> getAllEmployeesCount() {
		return ResponseEntity.ok(emplService.getCount());
	}

	
	// POST /rest-demo-app/employee/salary/DEVELOPER/2000
	@PostMapping(value="/salary/job/{job}/{increment}")
	@Operation(summary = "Increase employee salary by job")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully fired", content = @Content),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	@Transactional
	public ResponseEntity<Integer> increaseSalaryByJob(
			@PathVariable @ValueOfEnum(enumClass = Job.class, availableValues = "DEVELOPER,MANAGER,ANALYST") String job,
			@PathVariable @Positive Double increment) {
		int updatedCount = emplService.increaseSalaryByJob(increment, job);

		return ResponseEntity.status(HttpStatus.OK.value()).body(updatedCount);
	}
	
	// POST /rest-demo-app/employee/fire/MANAGER
	@PostMapping(value="/fire/job/{job}")
	@Operation(summary = "Fire employee by job")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully fired", content = @Content),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	//@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<Integer> fireByJob(@PathVariable @ValueOfEnum(enumClass = Job.class, availableValues = "DEVELOPER,MANAGER,ANALYST") String job) {
		int firedCount = emplService.fireByJob(job);
		return ResponseEntity.status(HttpStatus.OK.value()).body(firedCount);
	}
	
	// POST /rest-demo-app/employee/fire/MANAGER
	@PostMapping(value="/fire/fullname/{name}/{surname}")
	@Operation(summary = "Fire employee by job")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully fired", content = @Content),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<Integer> fireByFullname(
			@PathVariable @NotBlank String name,
			@PathVariable @NotBlank String surname
			) throws EmployeeNotFoundException {
		int firedCount = emplService.fireByFullname(name, surname);
		return ResponseEntity.status(HttpStatus.OK.value()).body(firedCount);
	}
	
	
	
	// POST /rest-demo-app/employee
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Insert new employee on db")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Employee successfully created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<EmployeeDTO> insertEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
		EmployeeDTO dto = emplService.saveNewEmployee(employeeDTO).convertEntityToDto();
		return ResponseEntity.status(HttpStatus.CREATED.value()).body(dto);
	}
	
	// PUT /rest-demo-app/employee/4
	@PutMapping(value="/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updating existing employee")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)) }),
	        @ApiResponse(responseCode = "204", description = "Employee not found"),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	@Transactional
	public ResponseEntity<EmployeeDTO> updateEmployee(
			@Valid @RequestBody EmployeeDTO employeeDTO, 
			@PathVariable @Positive Integer id) throws EmployeeNotFoundException {
		Employee existingEmp = emplService.updateExistingEmployee(employeeDTO, id);
		return ResponseEntity.status(HttpStatus.OK.value()).body(existingEmp.convertEntityToDto());
	}
	
	// DELETE /rest-demo-app/employee/4
	@DeleteMapping(value="/{id}")
	@Operation(summary = "Deleting existing employee by id")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully deleted", content = @Content),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	@Transactional
	public ResponseEntity<Long> deleteEmployee(@PathVariable @Positive Integer id) throws EmployeeNotFoundException {
		emplService.deleteEmployee(id);
		return ResponseEntity.noContent().build();
	}
	
	// DELETE /rest-demo-app/employee/fullname/mario/rossi
	@DeleteMapping(value="/fullname/{name}/{surname}")
	@Operation(summary = "Deleting employees by name and surname")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully deleted", content = @Content),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	@Transactional
	public ResponseEntity<Long> deleteEmployeeByFullname(
			@PathVariable @NotBlank String name,
			@PathVariable @NotBlank String surname
			) throws EmployeeNotFoundException {
		emplService.deleteEmployee(name, surname);
		return ResponseEntity.noContent().build();
	}
	
	// DELETE /rest-demo-app/employee/birthdate/2000-01-01
	@DeleteMapping(value="/birthdate/{birthdate}")
	@Operation(summary = "Deleting employees by birthdate")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully deleted", content = @Content),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	@Transactional
	public ResponseEntity<Long> deleteEmployeeByBirthDateGreaterThan(@PathVariable @CustomDatePattern(dateFormat = "yyyy-MM-dd") Date birthdate) {
		return ResponseEntity.ok(emplService.deleteYoungestEmployees(birthdate));
	}
	
	// DELETE /rest-demo-app/employee/all
	@DeleteMapping(value="/all")
	@Operation(summary = "Deleting all employees")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employee successfully deleted", content = @Content),
	        @ApiResponse(responseCode = "400", description = "Invalid request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "401", description = "Missing request authentication", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) }),
	        @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomError.class)) })}
	)
	@Transactional
	public ResponseEntity<Long> deleteAllEmployees() {
		emplService.deleteAll();
		return ResponseEntity.noContent().build();
	}

}

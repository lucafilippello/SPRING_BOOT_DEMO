package it.technologydata.tech4tech.FullRestApp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import io.micrometer.common.util.StringUtils;
import it.technologydata.tech4tech.FullRestApp.dto.EmployeeDTO;
import it.technologydata.tech4tech.FullRestApp.entity.Employee;
import it.technologydata.tech4tech.FullRestApp.exception.EmployeeNotFoundException;
import it.technologydata.tech4tech.FullRestApp.repository.EmployeeRepository;
import it.technologydata.tech4tech.FullRestApp.utility.AppConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository emplRepo;
	
	public List<Employee> getAllEmployees(String sortBy, Direction dir) {
		
		List<Employee> employees = new ArrayList<>();
		if(!StringUtils.isBlank(sortBy)) {
			employees = emplRepo.findAll(Sort.by(dir,sortBy));
		} else {
			employees = emplRepo.findAll();
		}
		 
		return employees;
	}
	
	public List<Employee> getEmployeesByJob(String job) {
		return emplRepo.readByJobOrderByHireDateDesc(job);
	}

	public Employee getEmployeeById(Integer id) {
		
		Optional<Employee>  optional = emplRepo.findById(id);
		return optional.orElseThrow(() -> new EmployeeNotFoundException(""+id));
	}
	
	public Employee getEmployeeByFullName(String name, String surname) {
		
		Optional<Employee>  optional = emplRepo.findByNameAndSurnameAllIgnoreCase(name, surname);
		return optional.orElseThrow(() -> new EmployeeNotFoundException(name+" "+ surname));
	}
	
	public List<Employee> getOldestEmployees(Date birthDate) {
		return emplRepo.findByBirthDateLessThanEqual(birthDate);
	}
	
	public List<Employee> getYoungestEmployees(Date birthDate) {
		return emplRepo.findByBirthDateGreaterThanEqual(birthDate);
	}
	
	public List<Employee> getEmployeesWithHighestSeniority(Date hireDate) {
		return emplRepo.findEmpByHireDateLessThanEqual(hireDate);
	}
	
	public List<Employee> getEmployeesWithLowestSeniority(Date hireDate) {
		return emplRepo.findByHireDateGreaterThanEqual(hireDate);
	}
	
	public List<Employee> getSalaryUnderThresholdEmployees(Double salary) {
		return emplRepo.findEmpBySalaryLessThanEqualOrderBySalary(salary);
	}
	
	public List<Employee> getSalaryOverThresholdEmployees(Double salary) {
		return emplRepo.findBySalaryGreaterThanEqualOrderBySalary(salary);
	}
	
	public int getCountByJob(String job) {
		int count = emplRepo.countEmployeesByJob(job);
		//logger.info("count:"+count);
		return count;
	}
	
	public int getCount() {
		int count = (int) emplRepo.count();
		//logger.info("count:"+count);
		return count;
	}
	
	public long deleteYoungestEmployees(Date birthDate) {
		long deletedCount = emplRepo.deleteByBirthDateGreaterThan(birthDate);
		log.info("deleted:"+deletedCount);
		return deletedCount;
	}
	
	public int increaseSalaryByJob(Double salaryIncr, String job) {
		int updatedCount = emplRepo.increaseSalaryByJob(salaryIncr, job);
		return updatedCount;
	}
	
	public int increaseSalaryByFullname(Double salaryIncr, String name, String surname) {
		int updatedCount = emplRepo.increaseSalaryByFullname(salaryIncr, name,surname);
		return updatedCount;
	}
	
	public int fireByJob(String job) {
		int updatedCount = emplRepo.fireByJob(Calendar.getInstance().getTime(),job);
		return updatedCount;
	}
	
	public int fireByFullname(String name, String surname) {
		int updatedCount = emplRepo.fireByFullame(Calendar.getInstance().getTime(),name,surname);
		return updatedCount;
	}
	
	
	public Employee saveNewEmployee(@Valid EmployeeDTO dto) {
		log.info("INSERTING new employee");
		return emplRepo.saveAndFlush(dto.convertDtoToEntity());
	}
	
	public Employee updateExistingEmployee(@Valid EmployeeDTO dto, Integer id) {
		
		Employee savedEmp = null;
		Optional<Employee> opt = emplRepo.findById(id);
		if(opt.isPresent()) {
			log.info("UPDATING employee with id "+id);
			Employee existingEmp = opt.get();
			Employee updatedEmp = dto.convertDtoToEntity();
			existingEmp.setName(updatedEmp.getName());
			existingEmp.setSurname(updatedEmp.getSurname());
			existingEmp.setHireDate(updatedEmp.getHireDate());
			existingEmp.setBirthDate(updatedEmp.getBirthDate());
			existingEmp.setJob(updatedEmp.getJob());
			existingEmp.setSalary(updatedEmp.getSalary());
			savedEmp = emplRepo.saveAndFlush(existingEmp);
		} else {
			/*
			logger.info(String.format("Employee with id %d not found. INSERTING new employee", id));
			savedEmp = emplRepo.saveAndFlush(dto.convertDtoToEntity());
			*/
			throw new EmployeeNotFoundException(id.toString());
		}
		
		
		return savedEmp;
	}
	
	public void deleteEmployee(Integer id) {
        if (emplRepo.existsById(id)) {
        	log.info("DELETING employee with id "+id);
        	emplRepo.deleteById(id);
        } else {
            throw new EmployeeNotFoundException(id.toString());
        }
    }
	
	public void deleteEmployee(String name, String surname) {
        if (emplRepo.existsByNameAndSurnameAllIgnoreCase(name, surname)) {
        	log.info("DELETING employee "+name +" "+ surname);
        	emplRepo.deleteByNameAndSurnameAllIgnoreCase(name, surname);
        } else {
            throw new EmployeeNotFoundException(name +" "+ surname);
        }
    }
	
	
	public void deleteAll() {
		emplRepo.deleteAll();
	}
	
	public void initDb() throws ParseException {
		
		log.info("#######  Performing db init #########");
		
		List<EmployeeDTO> all = new ArrayList<>();
		EmployeeDTO dto = EmployeeDTO.builder()
		.name("Aldo")
		.surname("Baglio")
		.email("aldo.baglio@gmail.com")
		//.birthDate("05/07/1981")
		.birthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1981"))
		//.hireDate("09/07/2006")
		.hireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/07/2006"))
		.salary(37000D)
		.job("DEVELOPER")	
		.build();
		//saveNewEmployee(dto);
		all.add(dto);
		
		dto = new EmployeeDTO();
		dto.setName("Giovanni");
		dto.setSurname("Storti");
		dto.setEmail("giorvanni.storti@gmail.com");
		//dto.setBirthDate("05/07/1988");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1988"));
		//dto.setHireDate("09/07/2015");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/07/2015"));
		dto.setSalary(39000D);
		dto.setJob("DEVELOPER");
		all.add(dto);
		
		
		dto = EmployeeDTO.builder().build();
		dto.setName("Giacomo");
		dto.setSurname("Poretti");
		dto.setEmail("giacomo.poretti@gmail.com");
		//dto.setBirthDate("05/07/1990");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1990"));
		//dto.setHireDate("09/07/2015");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/07/2015"));
		dto.setSalary(45000D);
		dto.setJob("MANAGER");
		all.add(dto);
		
		dto = EmployeeDTO.builder().build();
		dto.setName("Tony");
		dto.setSurname("Stark");
		dto.setEmail("tony.stark@gmail.com");
		//dto.setBirthDate("05/07/1993");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1993"));
		//dto.setHireDate("09/07/2010");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/07/2010"));
		dto.setSalary(50000D);
		dto.setJob("MANAGER");
		all.add(dto);
		
		dto = EmployeeDTO.builder().build();
		dto.setName("John");
		dto.setSurname("Wick");
		dto.setEmail("john.wick@gmail.com");
		//dto.setBirthDate("05/07/1975");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1975"));
		//dto.setHireDate("09/07/2000");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/07/2000"));
		dto.setSalary(40000D);
		dto.setJob("ANALYST");
		all.add(dto);
		
		dto = EmployeeDTO.builder().build();
		dto.setName("John");
		dto.setSurname("Doe");
		dto.setEmail("john.doe@gmail.com");
		//dto.setBirthDate("05/07/1980");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1980"));
		//dto.setHireDate("09/01/2001");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/01/2001"));
		dto.setSalary(39000D);
		dto.setJob("ANALYST");
		all.add(dto);
		
		dto = EmployeeDTO.builder().build();
		dto.setName("Sherlock");
		dto.setSurname("Holmes");
		dto.setEmail("sherlock.holmes@gmail.com");
		//dto.setBirthDate("05/07/1975");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1975"));
		//dto.setHireDate("09/01/1998");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/01/1998"));
		dto.setSalary(59000D);
		dto.setJob("ANALYST");
		all.add(dto);
		
		dto = EmployeeDTO.builder().build();
		dto.setName("Peter");
		dto.setSurname("Parker");
		dto.setEmail("peter.parker@yahoo.com");
		//dto.setBirthDate("05/07/1981");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1981"));
		//dto.setHireDate("09/07/1995");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/07/1995"));
		dto.setSalary(45000D);
		dto.setJob("DEVELOPER");
		all.add(dto);
		
		dto = EmployeeDTO.builder().build();
		dto.setName("Bud");
		dto.setSurname("Spencer");
		dto.setEmail("bud.spencer@gmail.com");
		//dto.setBirthDate("05/07/1970");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1970"));
		//dto.setHireDate("09/07/1991");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/07/1991"));
		dto.setSalary(42000D);
		dto.setJob("DEVELOPER");
		all.add(dto);
		
		dto = EmployeeDTO.builder().build();
		dto.setName("Terence");
		dto.setSurname("Hill");
		dto.setEmail("t.hill@gmail.com");
		//dto.setBirthDate("05/07/1975");
		dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse("05/07/1974"));
		//dto.setHireDate("09/07/1994");
		dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse("09/07/1994"));
		dto.setSalary(52000D);
		dto.setJob("MANAGER");
		all.add(dto);
		
		List<Employee> employees = all.stream().map(empl -> empl.convertDtoToEntity()).collect(Collectors.toList());
		emplRepo.saveAllAndFlush(employees);
		
	}

}

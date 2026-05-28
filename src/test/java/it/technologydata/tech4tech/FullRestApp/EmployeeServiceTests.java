package it.technologydata.tech4tech.FullRestApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import it.technologydata.tech4tech.FullRestApp.entity.Employee;
import it.technologydata.tech4tech.FullRestApp.service.EmployeeService;

@SpringBootTest()
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Profile("integrationtest")
public class EmployeeServiceTests {
	
	@Autowired
	private EmployeeService emplService;
	
	@Test
	//@Transactional
    @DisplayName("Test 1: Empty/Init Test")
    @Order(1)
    @Rollback(value = false)
    public void emptyAndInitTest() throws ParseException{
		
		List<Employee> employees = emplService.getAllEmployees(null, null);
		Assertions.assertThat(employees.size()).isEqualTo(7); 
		
		emplService.deleteAll();
		
		
		employees = emplService.getAllEmployees(null, null);
		Assertions.assertThat(employees.size()).isEqualTo(0); 
		
		
		
		emplService.initDb();
		

		employees = emplService.getAllEmployees(null, null);
		Assertions.assertThat(employees.size()).isEqualTo(6); 
		
    }
	
	
	@Test
	//@Transactional
    @DisplayName("Test 2: Filter By birthdate Test")
    @Order(2)
    @Rollback(value = false)
    public void filterByBirthDateTest() throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date testDate = sdf.parse("1989-01-01");
		
		
		List<Employee> employees = emplService.getOldestEmployees(testDate);
		employees.stream().forEach(emp -> {
			Assertions.assertThat(emp.getBirthDate()).isBeforeOrEqualTo(testDate);
		});

		
    }
	
	
	@Test
	//@Transactional
    @DisplayName("Test 3: Count by Job Test")
    @Order(3)
    @Rollback(value = false)
    public void countByJobTest() throws ParseException{
		
		List<Employee> employees = emplService.getAllEmployees(null, null);
		long _manualDevCount = 0L;
		for (Employee emp : employees) {
			if("DEVELOPER".equalsIgnoreCase(emp.getJob())) {
				_manualDevCount++;
			}
		}
		final long manualDevCount = _manualDevCount;
		
		long developersCount = emplService.getCountByJob("DEVELOPER");
		employees.stream().forEach(emp -> {
			Assertions.assertThat(manualDevCount).isEqualTo(developersCount);
		});

		
    }
	
	
	@Test
	//@Transactional
    @DisplayName("Test 4: Ordering Test")
    @Order(4)
    @Rollback(value = false)
    public void orderingTest() throws ParseException{
		
		List<Employee> employees = emplService.getAllEmployees("birthDate", Sort.Direction.DESC);
		Date actualBirthDate = null;
		for (Employee emp : employees) {
			if(actualBirthDate == null) {   
				actualBirthDate = emp.getBirthDate();
			} else {
				Assertions.assertThat(emp.getBirthDate()).isBeforeOrEqualTo(actualBirthDate);
			}
			
		}
    }

}

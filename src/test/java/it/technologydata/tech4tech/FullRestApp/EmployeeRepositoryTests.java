package it.technologydata.tech4tech.FullRestApp;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import it.technologydata.tech4tech.FullRestApp.dto.EmployeeDTO;
import it.technologydata.tech4tech.FullRestApp.entity.Employee;
import it.technologydata.tech4tech.FullRestApp.repository.EmployeeRepository;
import it.technologydata.tech4tech.FullRestApp.utility.AppConstants;

//@DataJpaTest
@SpringBootTest()
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Profile("integrationtest")
public class EmployeeRepositoryTests /*extends FullRestAppApplicationTests*/{
	
	//private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	@Test
    @DisplayName("Test 1: Get Employees Test")
    @Order(1)
    @Rollback(value = false)
    public void getEmployeeTest(){
		
		List<Employee> employees = employeeRepository.findAll();
		Assertions.assertThat(employees.size()).isEqualTo(10); 
    }
	
	
	@Test
    @DisplayName("Test 2: Save Employee Test")
    @Order(2)
    @Rollback(value = false)
    public void saveEmployeeTest() throws ParseException{
		
		List<Employee> employees = employeeRepository.findAll();
		Assertions.assertThat(employees.size()).isEqualTo(6);
        //Action
		EmployeeDTO dto = EmployeeDTO.builder()
				.name("Ugo")
				.surname("Fantozzi")
				.email("ugo.bambocci@gmail.com")
				//.birthDate("05/07/1981")
				.birthDate(AppConstants.REQUEST_PATH_DATE_FORMAT.parse("1981-07-05"))
				//.hireDate("09/07/2006")
				.hireDate(AppConstants.REQUEST_PATH_DATE_FORMAT.parse("2006-07-09"))
				.salary(37000D)
				.job("DEVELOPER")	
				.build();

        employeeRepository.saveAndFlush(dto.convertDtoToEntity());
        
        employees = employeeRepository.findAll();
        Assertions.assertThat(employees.size()).isEqualTo(7);
    }
	
	@Test
    @DisplayName("Test 3: Get Employee By Job Test")
    @Order(3)
    @Rollback(value = false)
    public void getEmployeeByJobTest(){
		
		//Action
		List<Employee> developers = employeeRepository.findByJob("DEVELOPER");

		developers.stream().forEach(dev -> {
			Assertions.assertThat("DEVELOPER".equalsIgnoreCase(dev.getJob()));
		} );

    }
	
	@Test
    @DisplayName("Test 4: Fire By Job Test")
    @Order(4)
	@Transactional
    @Rollback(value = false)
    public void fireByJobTest(){
		
		//Action
		employeeRepository.findByJob("DEVELOPER").stream().forEach(dev -> {
			Assertions.assertThat(dev.getFireDate()).isNull();
		} );
		
		employeeRepository.fireByJob(new Date(), "DEVELOPER");
		
		// Needed for intermediate commit
		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();
		
		employeeRepository.findByJob("DEVELOPER").stream().forEach(dev -> {
			Assertions.assertThat(dev.getFireDate()).isNotNull();
		});
		

    }
	
	
	@Test
    @DisplayName("Test 5: Update Salary Test")
    @Order(5)
	@Transactional
    @Rollback(value = false)
    public void updateSalaryTest(){
		
		//Action
		List<Employee> developers = employeeRepository.findByJob("DEVELOPER");
		Double salarySumBefore = developers.stream()
				  .map(dev -> dev.getSalary())
				  .collect(Collectors.summingDouble(Double::doubleValue));
		int devCount = developers.size();
		double increment = 10000D;
		employeeRepository.increaseSalaryByJob(increment, "DEVELOPER");
		
		
		// Needed for intermediate commit
		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();
		
		developers = employeeRepository.findByJob("DEVELOPER");
		Double salarySumAfter = developers.stream()
				  .map(dev -> dev.getSalary())
				  .collect(Collectors.summingDouble(Double::doubleValue));

		Assertions.assertThat(salarySumAfter).isEqualTo(salarySumBefore + (devCount * increment));
		

    }
	

}

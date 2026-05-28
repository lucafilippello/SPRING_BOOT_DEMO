package it.technologydata.tech4tech.FullRestApp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import it.technologydata.tech4tech.FullRestApp.controller.EmployeeController;
import jakarta.validation.ConstraintViolationException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {FullRestAppApplication.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerTests {

	@Autowired
    private EmployeeController emplController;
	
	
	
	@Test
	@DisplayName("Test 1: get all emp")
	@Order(1)
    //@Rollback(value = false)
	public void testGetAllEmp() throws Exception {
		Assertions.assertThat(emplController.getAllEmployees("id", "ASC")).hasSize(6);
	}
	
	
	@Test
	@DisplayName("Test 2: get specific emp")
	@Order(2)
	public void testGetSpecificEmp() throws Exception {
		Assertions.assertThat(emplController.getEmployee(1)).hasFieldOrPropertyWithValue("email", "aldo.baglio@gmail.com");
	}

	@Test
	@DisplayName("Test 3: validation constraints volations")
	@Order(3)
	public void testConstraintViolationExceptionOnPathParam() throws Exception {
		Assertions.assertThatThrownBy(() -> {emplController.getEmployee(-1);}).isInstanceOf(ConstraintViolationException.class);
		Assertions.assertThatThrownBy(() -> {emplController.getAllEmployees("invalidField", null);}).isInstanceOf(ConstraintViolationException.class);
	}
}

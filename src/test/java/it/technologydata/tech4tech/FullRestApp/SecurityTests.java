package it.technologydata.tech4tech.FullRestApp;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.technologydata.tech4tech.FullRestApp.dto.EmployeeDTO;
import it.technologydata.tech4tech.FullRestApp.utility.AppConstants;

@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class SecurityTests {
	
	@Autowired
	private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;
    
    private EmployeeDTO testDTO;

    @BeforeEach
    public void init(WebApplicationContext wac) throws ParseException {
    	testDTO = EmployeeDTO.builder()
    			.name("Mario2")
    			.surname("Verdi2")
    			//.birthDate("05/07/1990")
    			.birthDate(AppConstants.REQUEST_PATH_DATE_FORMAT.parse("1990-07-05"))
    			//.hireDate("09/07/2015")
    			.hireDate(AppConstants.REQUEST_PATH_DATE_FORMAT.parse("2015-07-09"))
    			.email("mario2.verdi2@yahoo.it")
    			.salary(45000D)
    			.job("MANAGER")
    			.build();
    }

    @Test
    @DisplayName("Test 1: Put request with no auth")
	@Order(1)
    void testPutEmployeeNoAuth() throws Exception {
    	
    	mockMvc.perform(put("/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testDTO)))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("Test 2: Put request with wrong user")
	@Order(2)
    void testPutEmployeeWrongUser() throws Exception {
        
    	mockMvc.perform(put("/employee/1").with(httpBasic("admin3","wrongpwd"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testDTO)))
    			.andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("Test 3: Put request with correct user but wrong password")
	@Order(3)
    void testPutEmployeeWrongPassword() throws Exception {
        
    	mockMvc.perform(put("/employee/1").with(httpBasic("admin1","wrongpwd"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testDTO)))
    			.andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("Test 4: Put request with correct user but unauthorized")
	@Order(4)
    void testPutEmployeeUnauthorizedUser() throws Exception {
        
    	mockMvc.perform(put("/employee/1").with(httpBasic("hr1","@hr1@"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testDTO)))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("Test 5: Put request with correct user and authorization")
	@Order(5)
    void testPutEmployeeSuccessfull() throws Exception {
        
    	mockMvc.perform(put("/employee/1").with(httpBasic("admin1","#admin1#"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk());
    }

}


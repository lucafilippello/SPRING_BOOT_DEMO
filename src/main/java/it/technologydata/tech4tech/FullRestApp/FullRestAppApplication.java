package it.technologydata.tech4tech.FullRestApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import it.technologydata.tech4tech.FullRestApp.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(/*exclude = {SecurityAutoConfiguration.class}*/)
@OpenAPIDefinition(info = @Info(title = "Employees API", version = "2.0", description = "Employees Information"))
@SecurityScheme(name = "rest_demo_app", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@Slf4j
public class FullRestAppApplication implements CommandLineRunner {
	
	
	@Autowired
	private EmployeeService service;

	public static void main(String[] args) {
		SpringApplication.run(FullRestAppApplication.class, args);
	}

    //Utilizzato per centralizzare le configurazioni del serializzatore/deserializzatore

    @Bean
    @Primary
    ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        //module.addSerializer(LOCAL_DATETIME_SERIALIZER);
        return new ObjectMapper()
          .setSerializationInclusion(JsonInclude.Include.NON_NULL)
          .registerModule(module);
    }
    /*
	@Bean
	@Primary
	Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
	    return new Jackson2ObjectMapperBuilder().serializationInclusion(JsonInclude.Include.NON_NULL);
	}
    */

	/*
    @Bean
    @RequestScope
    MappingJackson2HttpMessageConverter converter(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        converter.setObjectMapper(builder.build());
        return converter;
    }
    */

	@Override
	public void run(String... args) throws Exception {
		
		service.initDb();
	}
	
	

}

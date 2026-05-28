package it.technologydata.tech4tech.FullRestApp.dto;

import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import it.technologydata.tech4tech.FullRestApp.entity.Employee;
import it.technologydata.tech4tech.FullRestApp.enums.Job;
import it.technologydata.tech4tech.FullRestApp.utility.AppConstants;
import it.technologydata.tech4tech.FullRestApp.validation.CustomDatePattern;
import it.technologydata.tech4tech.FullRestApp.validation.ValueOfEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO {
	
	@Schema(description = "Unique identifier (positive integer)",example = "123456")
	private Integer id;
	
	@NotBlank
	//@Schema(description = "Name",example = "Mario")
	private String name;
	
	@NotBlank
	//@Schema(description = "Surname",example = "Rossi")
	private String surname;
	
	@NotBlank
	@Email
	//@Schema(description = "Email",example = "mario.rossi@gmail.com")
	private String email;
	
	/*
	@CustomDatePattern(dateFormat = "dd/MM/yyyy")
	@NotBlank
	@Schema(description = "Birth date (format: dd/MM/yyy)",example = "01/01/1900")
	private String birthDate;
	*/

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
	@JsonFormat(pattern="dd/MM/yyyy")
	//@Schema(description = "Birth date (format: dd/MM/yyy)",example = "01/01/1900")
	private Date birthDate;
	
	/*
	@CustomDatePattern(dateFormat = "dd/MM/yyyy")
	@NotBlank
	@Schema(description = "Hire date (format: dd/MM/yyyy)",example = "01/01/1900")
	private String hireDate;
	*/
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
	@JsonFormat(pattern="dd/MM/yyyy")
	//@Schema(description = "Hire date (format: dd/MM/yyyy)",example = "01/01/1900")
	private Date hireDate;
	
	/*
	@Schema(description = "Fire date (format: dd/MM/yyy)",example = "01/01/1900")
	private String fireDate;
	*/
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern="dd/MM/yyyy")
	//@Schema(description = "Fire date (format: dd/MM/yyy)",example = "01/01/1900")
	private Date fireDate;
	
	@Positive
	//@Schema(description = "RAL",example = "40500")
	private Double salary;
	
	@ValueOfEnum(enumClass = Job.class, availableValues = "DEVELOPER,MANAGER,ANALYST")
	//@Schema(description = "Job role. Allowed values: DEVELOPER,MANAGER,ANALYST",example = "MANAGER")
	@NotBlank
	private String job;

	
	
	//Disaccoppiamente db data - input/output bean
	public Employee convertDtoToEntity() {
		Employee empl = Employee.builder().build();
		//empl.setId(this.id);
		empl.setName(this.name);
		empl.setSurname(this.surname);
		empl.setEmail(this.email);
		empl.setHireDate(this.hireDate);
		empl.setBirthDate(this.birthDate);
		/*
		try {
			empl.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.parse(this.hireDate));
			empl.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.parse(this.birthDate));
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		*/
		/*
		if(!StringUtils.isBlank(fireDate)) {
			try {
				empl.setFireDate(AppConstants.OUTPUT_DATE_FORMAT.parse(this.fireDate));
			} catch (ParseException e) {
				logger.error(e.getMessage());
			}
		}
		*/
		if(fireDate != null) {
			empl.setFireDate(this.fireDate);
		}
		empl.setJob(this.job);
		empl.setSalary(this.salary);
		return empl;
	}

	@Override
	public String toString() {
		return "EmployeeDTO [id=" + id + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate
				+ ", hireDate=" + hireDate + ", fireDate=" + fireDate + ", salary=" + salary + ", job=" + job + "]";
	}

	
	
	

}

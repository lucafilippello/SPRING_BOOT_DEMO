package it.technologydata.tech4tech.FullRestApp.entity;

import java.util.Date;

import it.technologydata.tech4tech.FullRestApp.dto.EmployeeDTO;
import it.technologydata.tech4tech.FullRestApp.utility.AppConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name="EMPLOYEE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="FIRST_NAME", length=50, nullable=false, unique=false)
	private String name;
	
	@Column(name="LAST_NAME", length=50, nullable=false, unique=false)
	private String surname;
	
	@Column(name="EMAIL", length=50, nullable=false, unique=false)
	private String email;
	
	@Column(name="BIRTH_DATE", nullable=false, unique=false)
	private Date birthDate;
	
	@Column(name="HIRE_DATE", nullable=false, unique=false)
	private Date hireDate;
	
	@Column(name="FIRE_DATE", nullable=true, unique=false)
	private Date fireDate;
	
	@Column(name="SALARY", nullable=false, unique=false)
	private Double salary;
	
	@Column(name="JOB", length=20, nullable=false, unique=false)
	private String job;
	
	
	//Disaccoppiamente db data - input/output bean
	public EmployeeDTO convertEntityToDto() {
		EmployeeDTO dto =  EmployeeDTO.builder().build();
		dto.setId(this.getId());
		dto.setName(this.name);
		dto.setSurname(this.surname);
		dto.setEmail(this.email);
		//dto.setBirthDate(AppConstants.OUTPUT_DATE_FORMAT.format(this.birthDate));
		dto.setBirthDate(this.birthDate);
		//dto.setHireDate(AppConstants.OUTPUT_DATE_FORMAT.format(this.hireDate));
		dto.setHireDate(this.hireDate);
		if(this.fireDate != null) {
			//dto.setFireDate(AppConstants.OUTPUT_DATE_FORMAT.format(this.fireDate));
			dto.setFireDate(this.fireDate);
		}
		dto.setJob(this.job);
		dto.setSalary(this.salary);
		return dto;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate
				+ ", hireDate=" + hireDate + ", fireDate=" + fireDate + ", salary=" + salary + ", job=" + job + "]";
	}
}

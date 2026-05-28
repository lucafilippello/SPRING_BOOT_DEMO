package it.technologydata.tech4tech.FullRestApp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.technologydata.tech4tech.FullRestApp.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
	
	//Derived queries
	List<Employee> findByJob(String job);
	
	Optional<Employee> findByNameAndSurnameAllIgnoreCase(String name, String surname);
	
	List<Employee> findEmployeeByJobOrderByHireDate(String job);
	
	List<Employee> findByJobOrderByHireDateDesc(String job);
	List<Employee> getByJobOrderByHireDateDesc(String job);
	List<Employee> readByJobOrderByHireDateDesc(String job);
	
	List<Employee> findBySalaryGreaterThanEqualOrderBySalary(Double salary);
	
	List<Employee> findEmpBySalaryLessThanEqualOrderBySalary(Double salary);
	
	List<Employee> findByHireDateGreaterThanEqual(Date hireDate);
	
	List<Employee> findEmpByHireDateLessThanEqual(Date hireDate);
	
	List<Employee> findByBirthDateGreaterThanEqual(Date birthDate);
	
	List<Employee> findByBirthDateLessThanEqual(Date birthDate);

	List<Employee> findByFireDateIsNotNull();
	
	int countEmployeesByJob(String job);

	boolean existsByNameAndSurnameAllIgnoreCase(String name, String surname);
	
	int deleteByBirthDateGreaterThan(Date birthDate);
	
	int deleteByNameAndSurnameAllIgnoreCase(String name, String surname);
	
	@Modifying
	@Query(value="update Employee e set e.salary = e.salary + :increment where UPPER(e.job) = UPPER(:job)")
	int increaseSalaryByJob(@Param("increment") Double increment, @Param("job") String job);
	
	@Modifying
	@Query("update Employee e set e.salary = e.salary + :increment where UPPER(e.name) = UPPER(:name) and UPPER(e.surname) = UPPER(:surname)")
	int increaseSalaryByFullname(@Param("increment") Double increment, @Param("name") String name, @Param("surname") String surname);
	
	@Modifying
	@Query("update Employee e set e.fireDate = :fireDate where UPPER(e.job) = UPPER(:job)")
	int fireByJob(@Param("fireDate") Date fireDate, @Param("job") String job);
	
	@Modifying
	@Query("update Employee e set e.fireDate = :fireDate where UPPER(e.name) = UPPER(:name) and UPPER(e.surname) = UPPER(:surname)")
	int fireByFullame(@Param("fireDate") Date fireDate, @Param("name") String name, @Param("surname") String surname);
	
	
	
	

}

package com.siva.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.siva.rest.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	List<Employee> findBySalaryGreaterThanEqual(double minSalary);

    
    List<Employee> findBySalaryLessThanEqual(double maxSalary);
}


	



package com.siva.rest.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siva.rest.exception.EmployeeNotFoundException;
import com.siva.rest.model.Employee;
import com.siva.rest.repository.EmployeeRepository;

@Service
public class Employeeservice {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {

        validateSalary(employee);
        calculateSalary(employee);

        return employeeRepository.save(employee);
    }

    public List<Employee> saveAllEmployees(List<Employee> employees) {

        for (Employee e : employees) {
            validateSalary(e);
            calculateSalary(e);
        }
        return employeeRepository.saveAll(employees);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {

        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isPresent()) {
            return employee.get();
        } else {
            throw new EmployeeNotFoundException(
                    "Employee not found with id " + id
            );
        }
    }

    public Employee updateEmployee(Long id, Employee employee) {

        Employee existing = getEmployeeById(id);

        existing.setName(employee.getName());
        existing.setEmail(employee.getEmail());
        existing.setDepartment(employee.getDepartment());
        existing.setSalary(employee.getSalary());

        validateSalary(existing);
        calculateSalary(existing);

        return employeeRepository.save(existing);
    }


    public Employee partialUpdateEmployee(
            Long id,
            Map<String, Object> updatedDetails) {

        Employee existingEmp = getEmployeeById(id);

        updatedDetails.forEach((key, value) -> {

            switch (key) {

                case "salary":
                    existingEmp.setSalary((Double) value);
                    break;

                case "email":
                    existingEmp.setEmail((String) value);
                    break;
            }
        });

        

        return employeeRepository.save(existingEmp);
    }

    public void deleteEmployeeById(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with id " + id
            );
        }
        employeeRepository.deleteById(id);
    }

    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

    private void validateSalary(Employee employee) {
        if (employee.getSalary() <= 0) {
            throw new RuntimeException("Salary must be greater than 0");
        }
    }

    private void calculateSalary(Employee employee) {

        double basic = employee.getSalary();
        double hra = basic * 0.30;
        double da  = basic * 0.05;
        double pf  = basic * 0.04;

        double finalSalary = basic + hra + da - pf;
        employee.setSalary(finalSalary);
    }
    
    public List<Employee> filterByMinSalary(double minSalary) {
        return employeeRepository.findBySalaryGreaterThanEqual(minSalary);
    }
    public List<Employee> filterByMaxSalary(double maxSalary) {
        return employeeRepository.findBySalaryLessThanEqual(maxSalary);
    }

}

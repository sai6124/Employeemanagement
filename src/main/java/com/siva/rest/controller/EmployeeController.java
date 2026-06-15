package com.siva.rest.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.siva.rest.model.Employee;
import com.siva.rest.service.Employeeservice;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private Employeeservice employeeservice;

    @GetMapping("/greet")
    public ResponseEntity<String> greet() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Hello 👋 Welcome to Employee Management API");
    }

    @GetMapping("/bye")
    public ResponseEntity<String> bye() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Bye 👋 Thanks for using Employee Management API");
    }

    
    @PostMapping
    public ResponseEntity<EntityModel<Employee>> saveEmployee(
            @RequestBody Employee employee) {

        Employee saved = employeeservice.saveEmployee(employee);

        EntityModel<Employee> entityModel = EntityModel.of(saved);

        Link getLink = linkTo(
                methodOn(EmployeeController.class)
                        .getEmployeeById(saved.getId())
        ).withRel("get");

        Link deleteLink = linkTo(
                methodOn(EmployeeController.class)
                        .deleteEmployeeById(saved.getId())
        ).withRel("delete");

        Link putLink = linkTo(
                methodOn(EmployeeController.class)
                        .updateEmployee(saved.getId(), saved)
        ).withRel("put");

        Link patchLink = linkTo(
                methodOn(EmployeeController.class)
                        .partialUpdateEmployee(saved.getId(), new HashMap<>())
        ).withRel("patch");

        entityModel.add(getLink, deleteLink, putLink, patchLink);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("info", "Employee saved successfully")
                .body(entityModel);
    }

    
    @PostMapping("/bulk")
    public ResponseEntity<List<Employee>> saveBulkEmployees(
            @RequestBody List<Employee> employees) {

        List<Employee> saved = employeeservice.saveAllEmployees(employees);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("info", "Bulk employees saved successfully")
                .body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {

        Employee employee = employeeservice.getEmployeeById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("info", "Data retrieved successfully")
                .body(employee);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {

        List<Employee> employees = employeeservice.getAllEmployees();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("info", "All employees data retrieved")
                .body(employees);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Long id) {

        employeeservice.deleteEmployeeById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("info", "Employee deleted successfully")
                .body("Employee deleted with id " + id);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllEmployees() {

        employeeservice.deleteAllEmployees();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("info", "All employees deleted")
                .body("All employees deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        Employee updated = employeeservice.updateEmployee(id, employee);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("info", "Employee updated successfully")
                .body(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Employee> partialUpdateEmployee(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updatedDetails) {

        Employee updated = employeeservice.partialUpdateEmployee(id, updatedDetails);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("info", "Employee updated partially")
                .body(updated);
    }

    @GetMapping("/rotanquotes")
    public ResponseEntity<String> getquotation() {

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForEntity(
                "https://dummyjson.com/quotes/random",
                String.class
        );
    }
    @GetMapping("/ramproj/{pincode}")
    public ResponseEntity<String> getPincodeDetails(@PathVariable int pincode) {

        RestTemplate restTemplate = new RestTemplate();

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Accept", "application/json");

        org.springframework.http.HttpEntity<Void> entity =
                new org.springframework.http.HttpEntity<>(headers);

        return restTemplate.exchange(
                "https://api.postalpincode.in/pincode/" + pincode,
                org.springframework.http.HttpMethod.GET,
                entity,
                String.class
        );
    }
    @GetMapping("/weatherreport")
    public ResponseEntity<String> getWeatherReport() {

        RestTemplate restTemplate = new RestTemplate();

        String url =
            "https://api.openweathermap.org/data/2.5/weather" +
            "?lat=17.6868" +
            "&lon=83.2185" +
            "&units=metric" +
            "&appid=4c0e51a6702acfa6a8e489a625209f20";

        return restTemplate.getForEntity(url, String.class);
    }
    @GetMapping("/minsalary")
    public ResponseEntity<List<Employee>> getEmployeesByMinSalary(
            @RequestParam double minSalary) {

        List<Employee> employees = employeeservice.filterByMinSalary(minSalary);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("info", "Employees fetched with minimum salary")
                .body(employees);
    }


    @GetMapping("/maxsalary")
    public ResponseEntity<List<Employee>> getEmployeesByMaxSalary(
            @RequestParam double maxSalary) {

        List<Employee> employees = employeeservice.filterByMaxSalary(maxSalary);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("info", "Employees fetched with maximum salary")
                .body(employees);
    }



   
}

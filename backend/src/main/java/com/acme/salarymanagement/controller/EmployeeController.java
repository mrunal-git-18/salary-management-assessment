package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.EmployeePageResponse;
import com.acme.salarymanagement.dto.FilterOptions;
import com.acme.salarymanagement.dto.SalarySummary;
import com.acme.salarymanagement.dto.SalaryUpdateRequest;
import com.acme.salarymanagement.model.Employee;
import com.acme.salarymanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200"})
public class EmployeeController {
    private final EmployeeService service;
    public EmployeeController(EmployeeService service) { this.service = service; }

    @GetMapping("/health") public HealthResponse health() { return new HealthResponse("ok"); }
    @GetMapping("/employees") public EmployeePageResponse employees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String department) {
        return service.list(page, size, search, country, department);
    }
    @GetMapping("/employees/{id}") public Employee employee(@PathVariable long id) { return service.get(id); }
    @PutMapping("/employees/{id}/salary") public Employee updateSalary(@PathVariable long id, @Valid @RequestBody SalaryUpdateRequest request) {
        return service.updateSalary(id, request.salary());
    }
    @GetMapping("/salary/summary") public List<SalarySummary> summary(@RequestParam(required = false) String country,
                                                                         @RequestParam(required = false) String department) {
        return service.summary(country, department);
    }
    @GetMapping("/meta/filters") public FilterOptions filters() { return service.filters(); }
    public record HealthResponse(String status) {}
}

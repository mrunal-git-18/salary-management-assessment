package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.EmployeePageResponse;
import com.acme.salarymanagement.dto.FilterOptions;
import com.acme.salarymanagement.dto.SalarySummary;
import com.acme.salarymanagement.model.Employee;
import com.acme.salarymanagement.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    public EmployeeService(EmployeeRepository repository) { this.repository = repository; }

    public EmployeePageResponse list(int page, int size, String search, String country, String department) {
        if (page < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page must be zero or greater");
        if (size < 1 || size > 100) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size must be between 1 and 100");
        long total = repository.count(search, country, department);
        List<Employee> items = repository.findPage(page * size, size, search, country, department);
        int pages = total == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new EmployeePageResponse(items, total, page, size, pages);
    }

    public Employee get(long id) {
        if (id < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee ID must be positive");
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    public Employee updateSalary(long id, BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0 || salary.scale() > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary must be non-negative and have at most 2 decimal places");
        }
        get(id);
        repository.updateSalary(id, salary.setScale(2, RoundingMode.HALF_UP));
        return get(id);
    }

    public List<SalarySummary> summary(String country, String department) {
        return repository.summarize(country, department).stream().map(s -> new SalarySummary(s.currency(), s.employeeCount(),
                s.averageSalary() == null ? BigDecimal.ZERO : s.averageSalary().setScale(2, RoundingMode.HALF_UP),
                s.minimumSalary(), s.maximumSalary())).toList();
    }

    public FilterOptions filters() { return new FilterOptions(repository.distinctValues("country"), repository.distinctValues("department")); }
}

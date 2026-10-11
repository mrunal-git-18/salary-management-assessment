package com.acme.salarymanagement.dto;

import com.acme.salarymanagement.model.Employee;
import java.util.List;

public record EmployeePageResponse(List<Employee> items, long totalItems, int page, int size, int totalPages) {}

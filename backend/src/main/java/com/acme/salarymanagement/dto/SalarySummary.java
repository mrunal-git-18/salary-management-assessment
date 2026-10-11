package com.acme.salarymanagement.dto;

import java.math.BigDecimal;

public record SalarySummary(String currency, long employeeCount, BigDecimal averageSalary,
                            BigDecimal minimumSalary, BigDecimal maximumSalary) {}

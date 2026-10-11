package com.acme.salarymanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SalaryUpdateRequest(@NotNull @DecimalMin(value = "0.00", inclusive = true, message = "Salary must be zero or greater") BigDecimal salary) {}

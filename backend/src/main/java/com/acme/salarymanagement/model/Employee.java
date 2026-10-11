package com.acme.salarymanagement.model;

import java.math.BigDecimal;

public record Employee(long id, String employeeCode, String firstName, String lastName, String email,
                       String department, String country, String jobTitle, String currency,
                       BigDecimal salary, String updatedAt) {
    public String fullName() { return firstName + " " + lastName; }
}

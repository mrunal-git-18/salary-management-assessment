package com.acme.salarymanagement.dto;

import java.util.List;

public record FilterOptions(List<String> countries, List<String> departments) {}

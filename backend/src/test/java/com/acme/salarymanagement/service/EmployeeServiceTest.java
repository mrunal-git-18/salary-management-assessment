package com.acme.salarymanagement.service;

import com.acme.salarymanagement.model.Employee;
import com.acme.salarymanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock EmployeeRepository repository;
    @InjectMocks EmployeeService service;

    @Test void rejectsNegativeSalary() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.updateSalary(1, new BigDecimal("-1.00")));
        assertEquals(400, ex.getStatusCode().value());
        verifyNoInteractions(repository);
    }

    @Test void rejectsSalaryWithMoreThanTwoDecimalPlaces() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.updateSalary(1, new BigDecimal("10.999")));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test void rejectsInvalidPageSize() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.list(0, 101, null, null, null));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test void missingEmployeeReturnsNotFound() {
        when(repository.findById(999)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.get(999));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test void updatesExistingEmployeeSalary() {
        Employee before = new Employee(1, "EMP00001", "Aarav", "Sharma", "employee00001@example.test", "Engineering", "India", "Engineer", "INR", new BigDecimal("50000.00"), "2026-01-01T09:00");
        Employee after = new Employee(1, "EMP00001", "Aarav", "Sharma", "employee00001@example.test", "Engineering", "India", "Engineer", "INR", new BigDecimal("52000.00"), "2026-01-01T09:00");
        when(repository.findById(1)).thenReturn(Optional.of(before), Optional.of(after));
        when(repository.updateSalary(1, new BigDecimal("52000.00"))).thenReturn(true);
        Employee result = service.updateSalary(1, new BigDecimal("52000.00"));
        assertEquals(new BigDecimal("52000.00"), result.salary());
        verify(repository).updateSalary(1, new BigDecimal("52000.00"));
    }
}

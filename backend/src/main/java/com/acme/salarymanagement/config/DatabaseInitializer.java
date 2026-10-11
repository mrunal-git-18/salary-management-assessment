package com.acme.salarymanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final JdbcTemplate jdbc;
    private final boolean seedEnabled;

    public DatabaseInitializer(JdbcTemplate jdbc, @Value("${app.seed.enabled:true}") boolean seedEnabled) {
        this.jdbc = jdbc;
        this.seedEnabled = seedEnabled;
    }

    @Override public void run(String... args) {
        jdbc.execute("CREATE TABLE IF NOT EXISTS employees (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, employee_code TEXT NOT NULL UNIQUE, " +
                "first_name TEXT NOT NULL, last_name TEXT NOT NULL, email TEXT NOT NULL UNIQUE, " +
                "department TEXT NOT NULL, country TEXT NOT NULL, job_title TEXT NOT NULL, " +
                "currency TEXT NOT NULL, salary NUMERIC NOT NULL CHECK (salary >= 0), updated_at TEXT NOT NULL)");
        jdbc.execute("CREATE INDEX IF NOT EXISTS idx_employee_country_department ON employees(country, department)");
        jdbc.execute("CREATE INDEX IF NOT EXISTS idx_employee_name ON employees(last_name, first_name)");
        jdbc.execute("CREATE INDEX IF NOT EXISTS idx_employee_code ON employees(employee_code)");
        if (seedEnabled && countEmployees() == 0) seedEmployees();
    }

    private long countEmployees() {
        Long count = jdbc.queryForObject("SELECT COUNT(*) FROM employees", Long.class);
        return count == null ? 0 : count;
    }

    private void seedEmployees() {
        String[] firstNames = {"Aarav", "Mia", "Noah", "Isha", "Liam", "Ava", "Arjun", "Sophia", "Oliver", "Anaya", "Ethan", "Zara", "Lucas", "Emma", "Rohan", "Amelia", "Kabir", "Isla", "Daniel", "Priya"};
        String[] lastNames = {"Sharma", "Patel", "Smith", "Brown", "Taylor", "Wilson", "Martin", "Lee", "Kumar", "Clark", "Wright", "Hall", "Young", "Allen", "Scott", "Green", "Baker", "Adams", "Nelson", "Carter"};
        String[] departments = {"Engineering", "Human Resources", "Finance", "Sales", "Marketing", "Operations", "Legal", "Customer Support", "Research", "IT"};
        String[][] locations = {{"India", "INR"}, {"United States", "USD"}, {"United Kingdom", "GBP"}, {"Germany", "EUR"}, {"Canada", "CAD"}, {"Australia", "AUD"}, {"Singapore", "SGD"}};
        String[] titles = {"Software Engineer", "Senior Engineer", "HR Specialist", "Financial Analyst", "Sales Manager", "Product Manager", "Data Analyst", "Operations Lead", "Designer", "Support Engineer"};
        Random random = new Random(20261009L);
        String sql = "INSERT INTO employees(employee_code,first_name,last_name,email,department,country,job_title,currency,salary,updated_at) VALUES(?,?,?,?,?,?,?,?,?,?)";
        for (int start = 1; start <= 10_000; start += 500) {
            List<Object[]> batch = new java.util.ArrayList<>();
            int end = Math.min(start + 499, 10_000);
            for (int i = start; i <= end; i++) {
                String first = firstNames[random.nextInt(firstNames.length)];
                String last = lastNames[random.nextInt(lastNames.length)];
                String[] location = locations[random.nextInt(locations.length)];
                String department = departments[random.nextInt(departments.length)];
                String title = titles[random.nextInt(titles.length)];
                BigDecimal salary = BigDecimal.valueOf(2_500_000L + random.nextInt(12_500_001)).movePointLeft(2).setScale(2, java.math.RoundingMode.HALF_UP);
                batch.add(new Object[]{String.format("EMP%05d", i), first, last, String.format("employee%05d@example.test", i), department, location[0], title, location[1], salary, LocalDateTime.of(2026, 1, 1, 9, 0).plusDays(i % 365).toString()});
            }
            jdbc.batchUpdate(sql, batch);
        }
    }
}

package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.model.Employee;
import com.acme.salarymanagement.dto.SalarySummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {
    private final JdbcTemplate jdbc;
    private static final RowMapper<Employee> MAPPER = (rs, rowNum) -> new Employee(
            rs.getLong("id"), rs.getString("employee_code"), rs.getString("first_name"), rs.getString("last_name"),
            rs.getString("email"), rs.getString("department"), rs.getString("country"), rs.getString("job_title"),
            rs.getString("currency"), rs.getBigDecimal("salary"), rs.getString("updated_at"));

    public EmployeeRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<Employee> findPage(int offset, int size, String search, String country, String department) {
        QueryParts parts = filters(search, country, department);
        List<Object> args = new ArrayList<>(parts.args()); args.add(size); args.add(offset);
        return jdbc.query("SELECT * FROM employees" + parts.where() + " ORDER BY id LIMIT ? OFFSET ?", MAPPER, args.toArray());
    }

    public long count(String search, String country, String department) {
        QueryParts parts = filters(search, country, department);
        Long result = jdbc.queryForObject("SELECT COUNT(*) FROM employees" + parts.where(), Long.class, parts.args().toArray());
        return result == null ? 0 : result;
    }

    public Optional<Employee> findById(long id) {
        return jdbc.query("SELECT * FROM employees WHERE id = ?", MAPPER, id).stream().findFirst();
    }

    public boolean updateSalary(long id, BigDecimal salary) {
        int changed = jdbc.update("UPDATE employees SET salary = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?", salary, id);
        return changed > 0;
    }

    public List<SalarySummary> summarize(String country, String department) {
        StringBuilder sql = new StringBuilder("SELECT currency, COUNT(*) employee_count, AVG(salary) average_salary, MIN(salary) minimum_salary, MAX(salary) maximum_salary FROM employees WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (country != null && !country.isBlank()) { sql.append(" AND country = ?"); args.add(country); }
        if (department != null && !department.isBlank()) { sql.append(" AND department = ?"); args.add(department); }
        sql.append(" GROUP BY currency ORDER BY currency");
        return jdbc.query(sql.toString(), (rs, n) -> new SalarySummary(rs.getString("currency"), rs.getLong("employee_count"),
                rs.getBigDecimal("average_salary"), rs.getBigDecimal("minimum_salary"), rs.getBigDecimal("maximum_salary")), args.toArray());
    }

    public List<String> distinctValues(String column) {
        if (!column.equals("country") && !column.equals("department")) throw new IllegalArgumentException("Unsupported column");
        return jdbc.query("SELECT DISTINCT " + column + " FROM employees ORDER BY " + column, (rs, n) -> rs.getString(1));
    }

    private QueryParts filters(String search, String country, String department) {
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (search != null && !search.isBlank()) {
            where.append(" AND (LOWER(employee_code) LIKE ? OR LOWER(first_name || ' ' || last_name) LIKE ? OR LOWER(email) LIKE ?)");
            String pattern = "%" + search.trim().toLowerCase() + "%";
            args.add(pattern); args.add(pattern); args.add(pattern);
        }
        if (country != null && !country.isBlank()) { where.append(" AND country = ?"); args.add(country); }
        if (department != null && !department.isBlank()) { where.append(" AND department = ?"); args.add(department); }
        return new QueryParts(where.toString(), args);
    }
    private record QueryParts(String where, List<Object> args) {}
}

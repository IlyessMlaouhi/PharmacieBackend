package org.example.pharmaciebackend.Repositories;

import org.example.pharmaciebackend.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

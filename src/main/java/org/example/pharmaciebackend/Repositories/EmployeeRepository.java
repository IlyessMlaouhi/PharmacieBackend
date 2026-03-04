package org.example.pharmaciebackend.Repositories;

import org.example.pharmaciebackend.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByEmail(String email);
    boolean existsByEmail(String email);

}

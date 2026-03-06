package org.example.pharmaciebackend.Repositories;

import org.example.pharmaciebackend.Entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByEmployeeId(Long employeeId);

    List<Shift> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    List<Shift> findByDateBetween(LocalDate from, LocalDate to);

    List<Shift> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate from, LocalDate to);
}

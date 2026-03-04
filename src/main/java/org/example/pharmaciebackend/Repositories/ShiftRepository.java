package org.example.pharmaciebackend.Repositories;

import org.example.pharmaciebackend.Entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByEmployeeId(Long employeeId);

    // all shifts for one employee on a specific day (overlap check)
    List<Shift> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    // all shifts in a date range (calendar view — all employees)
    List<Shift> findByDateBetween(LocalDate from, LocalDate to);

    // shifts for one employee in a date range (weekly hours validation + filtered calendar)
    List<Shift> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate from, LocalDate to);
}

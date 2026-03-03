package org.example.pharmaciebackend.Repositories;

import org.example.pharmaciebackend.Entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
}

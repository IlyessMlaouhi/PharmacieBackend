package org.example.pharmaciebackend.Entities;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

@Data
public class Schedule {

    private List<Shift> shifts;
}

package org.example.pharmaciebackend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pharmaciebackend.Entities.Occupation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String name;
    private Occupation occupation;
    private String phone;
    private String email;
    private double weeklyHours;
}
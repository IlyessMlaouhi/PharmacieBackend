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
    private String firstName;
    private String lastName;
    private Occupation occupation;
    private String email;
    private double weeklyHours;
}
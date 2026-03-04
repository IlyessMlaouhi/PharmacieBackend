package org.example.pharmaciebackend.Dtos;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pharmaciebackend.Entities.Occupation;
import org.hibernate.annotations.TenantId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Role is required")
    private Occupation occupation;

    @NotBlank(message = "Email is required")
    private String email;

    @Positive(message = "Weekly hours must be positive")
    @Max(value = 60, message = "Weekly hours cannot exceed 60")
    private double weeklyHours;
}
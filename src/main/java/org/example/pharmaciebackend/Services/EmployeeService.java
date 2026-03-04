package org.example.pharmaciebackend.Services;

import org.example.pharmaciebackend.Dtos.EmployeeRequest;
import org.example.pharmaciebackend.Dtos.EmployeeResponse;
import org.example.pharmaciebackend.Entities.Employee;
import org.example.pharmaciebackend.Exceptions.EmployeeException;
import org.example.pharmaciebackend.Repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmployeeException("An employee with email '" + request.getEmail() + "' already exists.");
        }
        Employee employee = mapToEntity(request);
        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    // ─── READ ALL ─────────────────────────────────────────────────────────────

    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new EmployeeException("No employees found.");
        }
        return employees.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─── READ BY ID ───────────────────────────────────────────────────────────

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeException("Employee with ID " + id + " not found."));
        return mapToResponse(employee);
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeException("Employee with ID " + id + " not found."));

        if (!employee.getEmail().equals(request.getEmail())
                && employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmployeeException("Email '" + request.getEmail() + "' is already used by another employee.");
        }

        employee.setName(request.getFirstName());
        employee.setOccupation(request.getOccupation());
        employee.setEmail(request.getEmail());
        employee.setWeeklyHours(request.getWeeklyHours());

        Employee updated = employeeRepository.save(employee);
        return mapToResponse(updated);
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeException("Employee with ID " + id + " not found.");
        }
        employeeRepository.deleteById(id);
    }

    // ─── MAPPER HELPERS ───────────────────────────────────────────────────────
    // Think of these like Angular's pipe or a MapStruct mapper

    private Employee mapToEntity(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getFirstName());
        employee.setOccupation(request.getOccupation());
        employee.setEmail(request.getEmail());
        employee.setWeeklyHours(request.getWeeklyHours());
        return employee;
    }

    public EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setFirstName(employee.getName());
        response.setOccupation(employee.getOccupation());
        response.setEmail(employee.getEmail());
        response.setWeeklyHours(employee.getWeeklyHours());
        return response;
    }
}
package org.example.pharmaciebackend.Services;

import jakarta.mail.MessagingException;
import org.example.pharmaciebackend.Dtos.ShiftRequest;
import org.example.pharmaciebackend.Dtos.ShiftResponse;
import org.example.pharmaciebackend.Entities.Employee;
import org.example.pharmaciebackend.Entities.Shift;
import org.example.pharmaciebackend.Exceptions.EmployeeException;
import org.example.pharmaciebackend.Exceptions.ShiftException;
import org.example.pharmaciebackend.Repositories.EmployeeRepository;
import org.example.pharmaciebackend.Repositories.ShiftRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    private  ShiftRepository shiftRepository;
    private  EmployeeRepository employeeRepository;
    private EmailService emailService;

    public ShiftService(ShiftRepository shiftRepository, EmployeeRepository employeeRepository, EmailService emailService) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }


    public ShiftResponse createShift(ShiftRequest request) throws MessagingException {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EmployeeException("Employee with ID " + request.getEmployeeId() + " not found."));

        validateShiftTimes(request);
        validateNoOverlap(request, null);
        validateWeeklyHours(employee, request, null);

        Shift shift = mapToEntity(request, employee);
        Shift saved = shiftRepository.save(shift);
        emailService.sendShiftNotification(employee.getEmail(), employee.getName(),saved);
        return mapToResponse(saved);
    }


    public List<ShiftResponse> getAllShifts() {
        return shiftRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public List<ShiftResponse> getShiftsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeException("Employee with ID " + employeeId + " not found.");
        }
        return shiftRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public List<ShiftResponse> getShiftsByDateRange(LocalDate from, LocalDate to) {
        return shiftRepository.findByDateBetween(from, to)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public List<ShiftResponse> getShiftsByEmployeeAndDateRange(Long employeeId, LocalDate from, LocalDate to) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeException("Employee with ID " + employeeId + " not found.");
        }
        return shiftRepository.findByEmployeeIdAndDateBetween(employeeId, from, to)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public ShiftResponse updateShift(Long id, ShiftRequest request) throws MessagingException {
        Shift existing = shiftRepository.findById(id)
                .orElseThrow(() -> new ShiftException("Shift with ID " + id + " not found."));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EmployeeException("Employee with ID " + request.getEmployeeId() + " not found."));

        validateShiftTimes(request);
        validateNoOverlap(request, id);
        validateWeeklyHours(employee, request, id);

        existing.setEmployee(employee);
        existing.setDate(request.getDate());
        existing.setStartTime(request.getStartTime());
        existing.setEndTime(request.getEndTime());
        existing.setDescription(request.getDescription());
        emailService.sendShiftNotification(employee.getEmail(), employee.getName(),existing);


        Shift updated = shiftRepository.save(existing);
        return mapToResponse(updated);
    }


    public void deleteShift(Long id) {
        if (!shiftRepository.existsById(id)) {
            throw new ShiftException("Shift with ID " + id + " not found.");
        }
        shiftRepository.deleteById(id);
    }


    private void validateShiftTimes(ShiftRequest request) {
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new ShiftException("Start time must be before end time.");
        }
    }

    private void validateNoOverlap(ShiftRequest request, Long excludeShiftId) {
        List<Shift> existingShifts = shiftRepository.findByEmployeeIdAndDate(
                request.getEmployeeId(), request.getDate());

        for (Shift existing : existingShifts) {
            if (excludeShiftId != null && existing.getId().equals(excludeShiftId)) continue;

            boolean overlaps = request.getStartTime().isBefore(existing.getEndTime())
                    && request.getEndTime().isAfter(existing.getStartTime());

            if (overlaps) {
                throw new ShiftException(
                        "This shift overlaps with an existing shift from "
                                + existing.getStartTime() + " to " + existing.getEndTime()
                                + " on " + request.getDate() + ".");
            }
        }
    }

    private void validateWeeklyHours(Employee employee, ShiftRequest request, Long excludeShiftId) {
        LocalDate weekStart = request.getDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd   = request.getDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Shift> weekShifts = shiftRepository.findByEmployeeIdAndDateBetween(
                employee.getId(), weekStart, weekEnd);

        double totalHours = weekShifts.stream()
                .filter(s -> excludeShiftId == null || !s.getId().equals(excludeShiftId))
                .mapToDouble(s -> Duration.between(s.getStartTime(), s.getEndTime()).toMinutes() / 60.0)
                .sum();

        double newShiftHours = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() / 60.0;
        double total = totalHours + newShiftHours;

        if (total > employee.getWeeklyHours()) {
            throw new ShiftException(
                    "Adding this shift would give " + String.format("%.1f", total) + "h this week, "
                            + "but " + employee.getName()+ " is contracted for " + employee.getWeeklyHours() + "h/week.");
        }
    }


    private Shift mapToEntity(ShiftRequest request, Employee employee) {
        Shift shift = new Shift();
        shift.setEmployee(employee);
        shift.setDate(request.getDate());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setDescription(request.getDescription());
        return shift;
    }

    public ShiftResponse mapToResponse(Shift shift) {
        ShiftResponse response = new ShiftResponse();
        response.setId(shift.getId());
        response.setEmployeeId(shift.getEmployee().getId());
        response.setEmployeeName(shift.getEmployee().getName());
        response.setDate(shift.getDate());
        response.setStartTime(shift.getStartTime());
        response.setEndTime(shift.getEndTime());
        response.setDescription(shift.getDescription());
        double hours = Duration.between(shift.getStartTime(), shift.getEndTime()).toMinutes() / 60.0;
        response.setDurationHours(Math.round(hours * 10.0) / 10.0);
        return response;
    }
}
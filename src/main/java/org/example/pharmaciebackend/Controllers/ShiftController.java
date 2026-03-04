package org.example.pharmaciebackend.Controllers;

import org.example.pharmaciebackend.Dtos.ShiftRequest;
import org.example.pharmaciebackend.Dtos.ShiftResponse;
import org.example.pharmaciebackend.Services.ShiftService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    // POST /api/v1/shifts
    @PostMapping
    public ResponseEntity<ShiftResponse> createShift(@Valid @RequestBody ShiftRequest request) {
        ShiftResponse response = shiftService.createShift(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/v1/shifts
    // Optional query params: ?from=2025-01-01&to=2025-01-07
    // Flutter calendar calls this to load shifts for a given week/month
    @GetMapping
    public ResponseEntity<List<ShiftResponse>> getShifts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from != null && to != null) {
            return ResponseEntity.ok(shiftService.getShiftsByDateRange(from, to));
        }
        return ResponseEntity.ok(shiftService.getAllShifts());
    }

    // GET /api/v1/shifts/employee/{employeeId}
    // Optional query params: ?from=2025-01-01&to=2025-01-07
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ShiftResponse>> getShiftsByEmployee(
            @PathVariable Long employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from != null && to != null) {
            return ResponseEntity.ok(shiftService.getShiftsByEmployeeAndDateRange(employeeId, from, to));
        }
        return ResponseEntity.ok(shiftService.getShiftsByEmployee(employeeId));
    }

    // PUT /api/v1/shifts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ShiftResponse> updateShift(
            @PathVariable Long id,
            @Valid @RequestBody ShiftRequest request) {
        return ResponseEntity.ok(shiftService.updateShift(id, request));
    }

    // DELETE /api/v1/shifts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }
}
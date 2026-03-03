package org.example.pharmaciebackend.Services;


import org.example.pharmaciebackend.Repositories.ShiftRepository;
import org.springframework.stereotype.Service;

@Service
public class ShiftService {


    private ShiftRepository shiftRepository;

    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }
}

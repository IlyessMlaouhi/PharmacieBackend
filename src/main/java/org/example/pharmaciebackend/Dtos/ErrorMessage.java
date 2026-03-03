package org.example.pharmaciebackend.Dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    public String message;

    public String code;
}
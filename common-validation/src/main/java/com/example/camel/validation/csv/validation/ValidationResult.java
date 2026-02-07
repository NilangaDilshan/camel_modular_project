package com.example.camel.validation.csv.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult<T> {
    private List<T> validRecords = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    // Adders, Getters, and Setters
    public void addError(int row, String message) {
        errors.add("Row " + row + ": " + message);
    }
}

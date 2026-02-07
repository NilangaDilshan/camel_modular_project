package com.example.camel.validation.csv.validation;

import java.util.List;

public class CsvValidator {
    public boolean validateRow(List<String> row) {
        if (row == null || row.isEmpty()) {
            return false;
        }

        // Check for null or empty values
        return row.stream().noneMatch(field -> field == null || field.trim().isEmpty());
    }

    public boolean validateNotNull(Object value) {
        return value != null;
    }

    public boolean validateNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public boolean validateMinLength(String value, int minLength) {
        return value != null && value.length() >= minLength;
    }
}

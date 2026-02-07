package com.example.camel.validation.csv.validation;

import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Set;
public class GenericCsvService {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> ValidationResult<T> validateCsv(InputStream inputStream, Class<T> clazz) throws Exception {
        ValidationResult<T> result = new ValidationResult<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // 1. Parse CSV to Beans
            List<T> beans = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            // 2. Iterate and Validate
            for (int i = 0; i < beans.size(); i++) {
                T bean = beans.get(i);
                Set<ConstraintViolation<T>> violations = validator.validate(bean);

                if (violations.isEmpty()) {
                    result.getValidRecords().add(bean);
                } else {
                    for (ConstraintViolation<T> violation : violations) {
                        result.addError(i + 1, violation.getMessage());
                    }
                }
            }
        }
        return result;
    }
}

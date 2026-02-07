package com.example.camel.validation.csv.dto;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalaryCsvDto {
    @CsvBindByName(column = "id")
    @NotNull(message = "Id is required")
    private Integer id;
    @CsvBindByName(column = "name")
    @NotBlank(message = "Name must not be blank or null")
    private String name;
    @CsvBindByName(column = "salary")
    @NotNull(message = "Salary must not be null")
    private Double salary;
}

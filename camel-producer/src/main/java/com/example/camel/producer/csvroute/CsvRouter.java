package com.example.camel.producer.csvroute;

import com.example.camel.producer.service.SalaryService;
import com.example.camel.validation.csv.dto.SalaryCsvDto;
import com.example.camel.validation.csv.validation.GenericCsvService;
import com.example.camel.validation.csv.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvRouter extends RouteBuilder {

    private final GenericCsvService genericCsvService;

    private final SalaryService salaryService;

    @Value("${csv.input.path:files/csv}")
    private String inputPath;

    @Value("${csv.success.path:.done}")
    private String successPath;

    @Value("${csv.error.path:.error}")
    private String errorPath;

    @Value("${csv.poll.delay:5000}")
    private int pollDelay;

    @Override
    public void configure() throws Exception {

        // Main route: validate and route based on validation result
        from("file:" + inputPath + "?move=" + successPath + "/${file:name}&moveFailed=" + errorPath
                + "/${file:name}&delay=" + pollDelay)
                .routeId("CSV-Validation-Route")
                .log("Processing file: ${header.CamelFileName}")
                .process(this::validateCsvFile)
                .choice()
                .when(exchangeProperty("isValid").isEqualTo(true))
                .log("File validation successful. Processing ${exchangeProperty.recordCount} records")
                .to("direct:processValidFile")
                .otherwise()
                .log("File validation failed. Errors: ${exchangeProperty.validationErrors}")
                .throwException(new IllegalArgumentException("CSV validation failed"))
                .end();

        // Process valid file - batch save all records
        from("direct:processValidFile")
                .routeId("Process-Valid-File")
                .process(exchange -> {
                    ValidationResult<SalaryCsvDto> result = exchange.getProperty("validationResult",
                            ValidationResult.class);
                    exchange.getIn().setBody(result.getValidRecords());
                })
                .doTry()
                .bean(salaryService, "saveSalaryDtoList")
                .log("Successfully saved ${exchangeProperty.recordCount} records from ${header.CamelFileName}")
                .doCatch(Exception.class)
                .log("Error saving records: ${exception.message}")
                .throwException(new RuntimeException("Failed to save records to database"))
                .end();
    }

    private void validateCsvFile(Exchange exchange) {
        try {
            // Get file details
            InputStream inputStream = exchange.getIn().getBody(InputStream.class);
            String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);

            log.info("Validating CSV file: {}", fileName);

            // Validate using GenericCsvService
            ValidationResult<SalaryCsvDto> result = genericCsvService.validateCsv(inputStream, SalaryCsvDto.class);

            // Set properties for routing decision
            boolean isValid = result.getErrors().isEmpty();
            exchange.setProperty("isValid", isValid);
            exchange.setProperty("validationResult", result);
            exchange.setProperty("recordCount", result.getValidRecords().size());
            exchange.setProperty("validationErrors", result.getErrors());

            if (!isValid) {
                log.error("Validation failed for file: {}. Errors: {}", fileName, result.getErrors());
            }

        } catch (Exception e) {
            log.error("Error validating CSV file: {}", e.getMessage(), e);
            exchange.setProperty("isValid", false);
            exchange.setProperty("validationErrors", List.of("Exception: " + e.getMessage()));
        }
    }
}
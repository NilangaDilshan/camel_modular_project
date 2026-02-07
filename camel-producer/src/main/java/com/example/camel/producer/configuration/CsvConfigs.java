package com.example.camel.producer.configuration;

import com.example.camel.validation.csv.validation.GenericCsvService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfigs {

    @Bean
    public GenericCsvService genericCsvService() {
        return new GenericCsvService();
    }
}

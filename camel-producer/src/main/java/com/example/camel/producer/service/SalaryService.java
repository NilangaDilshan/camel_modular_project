package com.example.camel.producer.service;

import com.example.camel.domain.SalaryEntity;
import com.example.camel.producer.transformers.SalaryMapper;
import com.example.camel.repository.SalaryEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class SalaryService {

    private final SalaryEntityRepository salaryEntityRepository;
    private final SalaryMapper salaryMapper;

    @Transactional
    public void saveSalary(com.example.camel.validation.csv.dto.SalaryCsvDto salaryCsvDto) {
        log.info("Processing Salary Csv DTO: {}", salaryCsvDto);
        try {
            var salaryEntity = salaryMapper.toEntity(salaryCsvDto);
            this.salaryEntityRepository.save(salaryEntity);
            log.info("Saved Salary Entity: {}", salaryEntity);
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate entry found for salaryId: {}. Skipping...", salaryCsvDto.getId());
        }
    }

    @Transactional
    public void saveSalaryDtoList(List<com.example.camel.validation.csv.dto.SalaryCsvDto> salaryCsvDtos) {
        log.info("Processing {} Salary DTOs", salaryCsvDtos.size());
        int savedCount = 0;
        int skippedCount = 0;

        for (com.example.camel.validation.csv.dto.SalaryCsvDto dto : salaryCsvDtos) {
            try {
                SalaryEntity entity = salaryMapper.toEntity(dto);
                salaryEntityRepository.save(entity);
                savedCount++;
                log.debug("Saved: salaryId={}, name={}", dto.getId(), dto.getName());
            } catch (DataIntegrityViolationException e) {
                skippedCount++;
                log.warn("Duplicate entry for salaryId: {}. Skipping...", dto.getId());
            }
        }

        log.info("Processing complete: {} saved, {} skipped (duplicates)", savedCount, skippedCount);
    }
}

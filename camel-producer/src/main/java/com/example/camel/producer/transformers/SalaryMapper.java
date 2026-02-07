package com.example.camel.producer.transformers;

import com.example.camel.domain.SalaryEntity;
import com.example.camel.validation.csv.dto.SalaryCsvDto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SalaryMapper {

    @Mapping(source = "id", target = "salaryId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", qualifiedByName = "trimName")
    SalaryEntity toEntity(SalaryCsvDto dto);

    @Mapping(source = "salaryId", target = "id")
    SalaryCsvDto toDto(SalaryEntity entity);

    // List mappings - MapStruct automatically generates these
    List<SalaryEntity> toEntityList(List<SalaryCsvDto> dtos);

    List<SalaryCsvDto> toDtoList(List<SalaryEntity> entities);

    @Named("trimName")
    default String trimName(String name) {
        return name != null ? name.trim().toUpperCase() : null;
    }
}

package com.example.camel.repository;

import com.example.camel.domain.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryEntityRepository extends JpaRepository<SalaryEntity, Long> {
}

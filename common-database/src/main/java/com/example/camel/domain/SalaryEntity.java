package com.example.camel.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "salary")
@Getter
@Setter
public class SalaryEntity extends AbstractAuditEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8112569867983736123L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Team_SEQ")
    private Integer id;

    @Column(name = "salary_id", nullable = false)
    private Integer salaryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "salary", nullable = false)
    private Double salary;
}

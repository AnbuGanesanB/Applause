package com.example.award.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AwardDistribution {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Integer id;

    private Integer employeeId;

    private String empUuid;

    private String employeeName;

    private String awardName;

    private String description;

    private int points;
}

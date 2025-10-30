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
public class AwardType {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    private String description;

    private String category;

    private int points;

}

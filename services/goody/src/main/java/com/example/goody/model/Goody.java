package com.example.goody.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Goody {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Integer id;

    private String name;
    private int availableQuantity;
    private int points;

    @Version
    private long version;

}

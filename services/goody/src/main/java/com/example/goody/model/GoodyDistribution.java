package com.example.goody.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GoodyDistribution {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Integer id;

    private Integer employeeId;
    private String empUuid;
    private String employeeName;
    private int points;
    private int qty;
    private Integer goodyId;
    private String goodyName;
    private LocalDateTime orderingTime;
    private LocalDateTime cancelledTime;
    private LocalDateTime deliveredTime;
    private LocalDateTime rejectedTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderStatus status;

}

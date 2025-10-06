package com.example.employee.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String empName;

    private String firstName;

    private String lastName;

    private String uuid;

    @ManyToOne
    @JoinColumn(name="manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private List<Department> teams = new ArrayList<>();

}

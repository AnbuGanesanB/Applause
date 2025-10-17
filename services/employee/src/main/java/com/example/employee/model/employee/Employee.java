package com.example.employee.model.employee;

import com.example.employee.model.department.Department;
import com.example.employee.model.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String empName;

    private String firstName;

    private String lastName;

    private String uuid;

    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

    @OneToMany(mappedBy = "manager")
    private Set<Department> departmentsManaged = new HashSet<>();

    @OneToMany(mappedBy = "teamLead")
    private Set<Team> teamsLed = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    private Set<Team> teams = new HashSet<>();

}

package com.example.employee.model.department;

import com.example.employee.model.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(unique = true)
    private String departmentName;

    @OneToMany(mappedBy = "department")
    private Set<Employee> deptMembers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="manager")
    private Employee manager;
}

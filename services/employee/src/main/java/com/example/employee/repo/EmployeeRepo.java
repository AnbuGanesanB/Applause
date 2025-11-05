package com.example.employee.repo;

import com.example.employee.model.employee.Employee;
import com.example.employee.model.employee.EmployeeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {

    Optional<Employee> findByUuid(String empUuid);
}

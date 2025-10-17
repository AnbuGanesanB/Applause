package com.example.employee.service;
import com.example.employee.dtos.NewUser;
import com.example.employee.kafka.EmpInfo;
import com.example.employee.kafka.EmployeeProducer;

import com.example.employee.model.department.Department;
import com.example.employee.model.employee.Employee;
import com.example.employee.model.employee.EmployeeDto;
import com.example.employee.model.employee.EmployeeDtoMapper;
import com.example.employee.repo.EmployeeRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.employee.ApiConstant.API_VERSION;

@Service
@RequiredArgsConstructor
@RequestMapping(API_VERSION)
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeProducer employeeProducer;
    private final EntityRetrieve entityRetrieve;
    private final KeycloakCommunicateService keycloakCommunicateService;
    private final EmployeeDtoMapper employeeDtoMapper;


    public Employee createUser(NewUser newUser) {
        UserRepresentation createdUserRepresentation = keycloakCommunicateService.createUser(newUser);
        return addEmployeeFromKeycloak(createdUserRepresentation);
    }

    private Employee addEmployeeFromKeycloak(UserRepresentation userRepresentation){
        Employee employee = new Employee();
        employee.setUuid(userRepresentation.getId());
        System.out.println("Processing user: "+userRepresentation.getUsername());

        if(employeeRepo.exists(Example.of(employee))) return null;

        employee.setFirstName(userRepresentation.getFirstName());
        employee.setLastName(userRepresentation.getLastName());
        employee.setEmpName(userRepresentation.getUsername());

        return employeeRepo.save(employee);
    }

    @Transactional
    public Employee updateDepartment(int employeeId, int departmentId) {
        Employee employee = entityRetrieve.getEmployeeById(employeeId);

        Department department = null;
        if (departmentId > 0) {
            department = entityRetrieve.getDepartmentById(departmentId);
        }

        employee.setDepartment(department);
        return employeeRepo.save(employee);

        // Trigger Notification for employee and manager
    }

    @Transactional
    public void syncUsersFromKeycloak() {
        List<UserRepresentation> currentUsers = keycloakCommunicateService.getUsersFromKeycloak();
        for (UserRepresentation userRepresentation: currentUsers){
            addEmployeeFromKeycloak(userRepresentation);
        }
    }

    public void sendKafkaMessage(){
        employeeProducer.sendEmpData(new EmpInfo("Username","fName","lName","email"));
        System.out.println("Service method completed");
    }

    public EmployeeDto getEmployeeDetails(Employee employee){
        return employeeDtoMapper.getEmployeeDetails(employee);
    }

    public List<EmployeeDto> getAllEmployeeDetails(){
        return employeeRepo.findAll().stream().map(this::getEmployeeDetails).collect(Collectors.toList());
    }
}

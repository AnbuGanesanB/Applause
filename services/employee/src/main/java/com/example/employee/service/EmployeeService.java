package com.example.employee.service;

import com.example.employee.dtos.NewUser;
import com.example.employee.feignClients.KeycloakClient;
import com.example.employee.kafka.EmpInfo;
import com.example.employee.kafka.EmployeeProducer;
import com.example.employee.mapper.Mapper;
import com.example.employee.model.Employee;
import com.example.employee.repo.EmployeeRepo;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final KeycloakClient keycloakClient;
    private final Mapper mapper;
    private final Keycloak keycloak;
    private final EmployeeRepo employeeRepo;
    private final EmployeeProducer employeeProducer;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    /*private String getAccessToken() {

        Map<String,String> params = new HashMap<>();
        params.put("client_id",clientId);
        params.put("client_secret",clientSecret);
        params.put("grant_type","client_credentials");
        KeycloakTokenResponse response = keycloakClient.getServiceToken(
                realm,params
        );
        System.out.println("Response:"+response.getAccessToken());
        return response.getAccessToken();
    }

    public List<Map<String, Object>> getAllUsers() {
        return keycloakClient.getUsers("Bearer " + getAccessToken(), realm);
    }


    public void addNewUser(NewUser newUser) {
        String serviceToken = getAccessToken();
        keycloakClient.addUser("Bearer "+serviceToken, mapper.getNewUser(newUser));

    }*/

    public Optional<UserRepresentation> getUserByUsername(String username) {
        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .search(username, true);

        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    /**
     * Create new user
     */
    public void createUser(NewUser newUser) {
        UserRepresentation user = new UserRepresentation();

        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Create user
        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() == 201) {
            UserRepresentation createdUser = getUserByUsername(user.getUsername())
                    .orElseThrow(() -> new RuntimeException("User created, but not found"));
            String userId = createdUser.getId();
            setUserPassword(userId, newUser.getUsername(), false);
            addEmployeeToEmpService(createdUser);
        } else {
            String resBody = response.readEntity(String.class);
            System.err.println("Body: " + resBody);
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
        }
    }

    /**
     * Set user password
     */
    public void setUserPassword(String userId, String password, boolean temporary) {
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(temporary);

        keycloak.realm(realm).users().get(userId).resetPassword(cred);
    }

    /**
     * Delete user by ID
     */
    public void deleteUser(String userId) {
        keycloak.realm(realm).users().get(userId).remove();
    }

    private List<UserRepresentation> getUsers() {
        return keycloak
                .realm(realm)
                .users()
                .list();
    }

    public void syncUsersFromKeycloak() {
        List<UserRepresentation> currentUsers = getUsers();
        for (UserRepresentation userRepresentation: currentUsers){
            addEmployeeToEmpService(userRepresentation);
        }
    }

    private void addEmployeeToEmpService(UserRepresentation userRepresentation){
        Employee employee = new Employee();
        employee.setUuid(userRepresentation.getId());
        System.out.println("Processing user: "+userRepresentation.getUsername());

        if(employeeRepo.exists(Example.of(employee))) return;

        employee.setFirstName(userRepresentation.getFirstName());
        employee.setLastName(userRepresentation.getLastName());
        employee.setEmpName(userRepresentation.getUsername());

        employeeRepo.save(employee);
    }

    public void sendKafkaMessage(){
        employeeProducer.sendEmpData(new EmpInfo("Username","fName","lName","email"));
        System.out.println("Service method completed");
    }

}

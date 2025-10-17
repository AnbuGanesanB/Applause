package com.example.employee.service;

import com.example.employee.dtos.NewUser;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeycloakCommunicateService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    /**
     * Create new user
     */
    public UserRepresentation createUser(NewUser newUser){
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
            return createdUser;
            //employeeService.addEmployeeToEmpService(createdUser);
        } else {
            String resBody = response.readEntity(String.class);
            System.err.println("Body: " + resBody);
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
        }
    }

    /**
     * Retrieve User from Keycloak by username
     */
    private Optional<UserRepresentation> getUserByUsername(String username) {
        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .search(username, true);

        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    /**
     * Set user password
     */
    private void setUserPassword(String userId, String password, boolean temporary) {
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(temporary);

        keycloak.realm(realm).users().get(userId).resetPassword(cred);
    }

    /** Retrieve all users from keycloak
     */
    public List<UserRepresentation> getUsersFromKeycloak() {
        return keycloak
                .realm(realm)
                .users()
                .list();
    }

}

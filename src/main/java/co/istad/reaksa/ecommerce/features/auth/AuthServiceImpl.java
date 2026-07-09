package co.istad.reaksa.ecommerce.features.auth;

import co.istad.reaksa.ecommerce.features.auth.dto.RegisterRequest;
import co.istad.reaksa.ecommerce.features.auth.dto.RegisterResponse;
import co.istad.reaksa.ecommerce.features.auth.dto.RoleEnum;
import co.istad.reaksa.ecommerce.features.userprofile.UserProfile;
import co.istad.reaksa.ecommerce.features.userprofile.UserProfileRepository;
import co.istad.reaksa.ecommerce.security.KeycloakAdminClientProps;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;


import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final Keycloak keycloak;
    private final KeycloakAdminClientProps props;
    private final AuthMapper authMapper;
    private final UserProfileRepository userProfileRepository;



    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        // Validate password matching
        if (!registerRequest.password().equals(registerRequest.confirmedPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords don't match!"
            );
        }

        // Create keycloak user
        UsersResource usersResource = keycloak
                .realm(props.getTargetRealm())
                .users();


        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(registerRequest.username());
        userRepresentation.setEmail(registerRequest.email());
        userRepresentation.setFirstName(registerRequest.firstName());
        userRepresentation.setLastName(registerRequest.lastName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);

        // Set Keycloak custom attributes
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phoneNumber", List.of(registerRequest.phoneNumber()));
        userRepresentation.setAttributes(attributes);

        // Set Credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerRequest.password());
        userRepresentation.setCredentials(List.of(credential));

        try (Response response = usersResource.create(userRepresentation)) {
            log.info("Response status code: {}", response.getStatus());
            if (response.getStatus() == HttpStatus.CREATED.value()) {
                // Succeed situation
                UserRepresentation createdUser = keycloak.realm(props.getTargetRealm())
                        .users()
                        .search(userRepresentation.getUsername())
                        .getFirst();
                log.info("Created user: {}", createdUser.getId());


                UserResource userResource = keycloak.realm(props.getTargetRealm()).users().get(createdUser.getId());
                userResource.sendVerifyEmail();
                RolesResource roleResource = keycloak.realm(props.getTargetRealm()).roles();
                RoleRepresentation roleUser = roleResource.get(RoleEnum.ROLE_USER.name()).toRepresentation();
                RoleRepresentation roleStudent = roleResource.get(RoleEnum.ROLE_CUSTOMER.name()).toRepresentation();
                log.info("role user: {}", roleUser);
                log.info("role student: {}", roleStudent);
                userResource.roles().realmLevel().add(List.of(roleUser,roleStudent));


                // Save user profile
                UserProfile userProfile = new UserProfile();
                userProfile.setUserId(createdUser.getId());
                userProfileRepository.save(userProfile);

                return authMapper.mapUserRepresentaionToRegisterResponse(createdUser);
            } else if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                // Conflict situation
                log.info("Check username or email already exists!");
            }
        }

        return null;
    }
}

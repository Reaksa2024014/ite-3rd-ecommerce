package co.istad.reaksa.ecommerce.features.userprofile;

import co.istad.reaksa.ecommerce.features.userprofile.dto.UserProfileResponse;
import co.istad.reaksa.ecommerce.security.KeycloakAdminClientProps;
import co.istad.reaksa.ecommerce.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    private final Keycloak keycloak;
    private final KeycloakAdminClientProps props;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse me() {
        // 1. Profile from Keycloak by userId
        String userId = SecurityUtils.extractUserId();
        UserRepresentation keycloakUser = keycloak.realm(props.getTargetRealm())
                .users()
                .get(userId)
                .toRepresentation();

        // 2. Profile from Database by userId
        return userProfileMapper.mapUserRepresentationToUserProfileResponse(keycloakUser);
    }

}
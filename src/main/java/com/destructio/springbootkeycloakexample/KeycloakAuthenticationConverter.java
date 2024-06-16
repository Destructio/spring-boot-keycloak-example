package com.destructio.springbootkeycloakexample;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.PREFERRED_USERNAME;

public class KeycloakAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var roles = getRolesFromJWT(jwt);
        String name = getNameFromJWT(jwt);

        return new JwtAuthenticationToken(jwt, roles, name);
    }

    @SuppressWarnings("unchecked")
    private static Collection<GrantedAuthority> getRolesFromJWT(Jwt jwt) {
        var realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
        var roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
        return getRolesFromJWT(roles);
    }

    private static String getNameFromJWT(Jwt jwt) {
        return jwt.getClaimAsString(PREFERRED_USERNAME);
    }

    private static Collection<GrantedAuthority> getRolesFromJWT(Collection<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}

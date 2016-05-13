package at.jku.cis.radar.filter;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;

import at.jku.cis.radar.annotations.Secured;
import at.jku.cis.radar.storage.TokenStorage;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String TOKEN_PREFIX = "RADAR";

    @Inject
    private TokenStorage tokenStorage;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String requestToken = determineRequestToken(requestContext);
        
        if (startsWithTokenPrefix(requestToken)) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        
        String token = requestToken.replace(TOKEN_PREFIX, StringUtils.EMPTY);
        if (!tokenStorage.containsToken(token)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        } else {
            requestContext.setSecurityContext(createSecurityContext(token));
        }
    }

    private String determineRequestToken(ContainerRequestContext requestContext) {
        return requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    }

    private boolean startsWithTokenPrefix(String token) {
        return StringUtils.isEmpty(token) || !token.startsWith(TOKEN_PREFIX);
    }

    private SecurityContext createSecurityContext(final String token) {
        return new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                return createPrincipal(token);
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return StringUtils.EMPTY;
            }
        };
    }

    private Principal createPrincipal(final String token) {
        return new Principal() {

            @Override
            public String getName() {
                return tokenStorage.getUsername(token);
            }
        };
    }
}
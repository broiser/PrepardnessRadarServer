package at.jku.cis.radar.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;

import at.jku.cis.radar.annotations.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String PREFIX_TOKEN = "RADAR";
    private static final String TOKEN = "token";

    @Inject
    private HttpSession httpSession;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(PREFIX_TOKEN)) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        try {
            validateToken(authorizationHeader.replace(PREFIX_TOKEN, StringUtils.EMPTY));
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validateToken(String token) throws Exception {
        String currentToken = (String) httpSession.getAttribute(TOKEN);
        if (currentToken != null && StringUtils.equals(currentToken, token)) {
            throw new IllegalArgumentException("Token doesn't match.");
        }
    }
}
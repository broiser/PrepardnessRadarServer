package at.jku.cis.radar.rest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import at.jku.cis.radar.model.Account;
import at.jku.cis.radar.model.AuthenticationToken;
import at.jku.cis.radar.service.AccountService;
import at.jku.cis.radar.storage.TokenStorage;

@Path("/authentication")
public class AuthenticationRestService extends RestService {
    private static final String FORMAT_TOKEN = "RADAR{0}";

    @Inject
    private TokenStorage tokenStorage;
    @Inject
    private AccountService accountService;

    @POST
    public Response authenticateAccount(Credentials credentials, @Context HttpServletRequest httpServletRequest) {
        try {
            authenticate(credentials.getUsername(), credentials.getPassword());
            String token = issueToken(credentials.getUsername());
            tokenStorage.storeToken(token, credentials.getUsername());
            return Response.ok(buildAuthenticationToken(token)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void authenticate(String username, String password) throws Exception {
        Account account = accountService.findByUsername(username);
        if (account == null || !(password.equals(account.getPassword()))) {
            throw new LoginException();
        }
    }

    private String issueToken(String username) {
        return new BigInteger(130, new SecureRandom(username.getBytes())).toString(32);
    }

    private AuthenticationToken buildAuthenticationToken(String token) {
        return new AuthenticationToken(MessageFormat.format(FORMAT_TOKEN, token));
    }

    @SuppressWarnings("unused")
    private static class Credentials {
        private String username;
        private String password;

        public Credentials() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

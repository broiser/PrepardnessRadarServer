package at.jku.cis.radar.rest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Random;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.jku.cis.radar.model.Account;
import at.jku.cis.radar.service.AccountService;

@Path("/authentication")
public class AuthenticationRestService extends RestService {
    private static final String FROMAT_TOKEN = "RADAR{0}";
    private static final String TOKEN = "radarToken";

    @Inject
    private AccountService accountService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticateAccount(Credentials credentials, @Context HttpServletRequest httpServletRequest) {
        try {
            authenticate(credentials.getUsername(), credentials.getPassword());
            String token = issueToken(credentials.getUsername());
            httpServletRequest.getSession().setAttribute(TOKEN, token);
            return Response.ok(MessageFormat.format(FROMAT_TOKEN, token)).build();
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
        Random random = new SecureRandom(username.getBytes());
        return new BigInteger(130, random).toString(32);
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

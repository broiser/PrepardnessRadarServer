package at.jku.cis.radar.producer;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import at.jku.cis.radar.annotations.CurrentAccount;
import at.jku.cis.radar.model.Account;
import at.jku.cis.radar.service.AccountService;

@ApplicationScoped
public class CurrentAccountProducer implements Serializable {

    private static final String USERNAME = "username";

    @Inject
    private AccountService accountService;

    @Produces
    @RequestScoped
    @CurrentAccount
    public Account productAccount(@Context HttpServletRequest httpServletRequest) {
        String username = determineUsername(httpServletRequest);
        return accountService.findByUsername(username);
    }

    private String determineUsername(HttpServletRequest httpServletRequest) {
        return (String) httpServletRequest.getSession().getAttribute(USERNAME);
    }
}

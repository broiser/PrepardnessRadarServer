package at.jku.cis.radar.dao;

import javax.enterprise.context.ApplicationScoped;

import at.jku.cis.radar.model.Account;

@ApplicationScoped
public class AccountDao extends AbstractDao<Account> {

    public AccountDao() {
        super(Account.class);
    }

    public Account findByUsername(String username) {
        return getSingleResult(createNamedQuery(Account.FIND_BY_USERNAME).setParameter("username", username));
    }

}

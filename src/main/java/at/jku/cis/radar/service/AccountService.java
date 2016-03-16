package at.jku.cis.radar.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import at.jku.cis.radar.dao.AccountDao;
import at.jku.cis.radar.model.Account;

@ApplicationScoped
public class AccountService implements Serializable{

    @Inject
    private AccountDao accountDao;
    
    public Account findByUsername(String username){
        return accountDao.findByUsername(username);
    }
    
}

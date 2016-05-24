package com.revolut.facade.orm;

import com.revolut.facade.model.Account;
import com.revolut.facade.model.User;
import org.hibernate.Session;

import static com.revolut.facade.orm.HibernateUtil.saveOrUpdateEntity;

/**
 * Created by azhuravel on 22.05.16.
 */
public class DatabaseService {
    public User getUser(long id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(User.class, id);
        }
    }

    public Long saveUser(User user) {
        return saveOrUpdateEntity(user);
    }

    public Long saveAccount(Account account) {
        return saveOrUpdateEntity(account);
    }
}

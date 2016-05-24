package com.revolut.facade.orm;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.revolut.facade.model.Account;
import com.revolut.facade.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

/**
 * Created by azhuravel on 22.05.16.
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Account.class)
                    .addAnnotatedClass(User.class)
                    .configure("hibernate/hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    public static Long saveOrUpdateEntity(Object entity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Long id = (Long) session.save(entity);

        session.getTransaction().commit();
        return id;
    }
}

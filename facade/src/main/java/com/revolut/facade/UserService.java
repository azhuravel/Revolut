package com.revolut.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.facade.model.Account;
import com.revolut.facade.model.Transaction;
import com.revolut.facade.model.User;
import com.revolut.facade.orm.DatabaseService;
import com.revolut.facade.orm.HibernateUtil;

import static spark.Service.ignite;
import static spark.Spark.*;
import org.hibernate.LockMode;
import org.hibernate.Session;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Service;

import java.io.IOException;

/**
 * Created by azhuravel on 19.05.16.
 */
public class UserService {

    private DatabaseService databaseService;

    private ObjectMapper mapper = new ObjectMapper();

    public UserService(DatabaseService databaseService, int port) {
        this.databaseService = databaseService;

        port(port);

        // users
        get("/users/:userId", this::getUser);
        post("/users", this::addUser);

        // account
        post("/users/:userId/accounts", this::addAccount);
        get("/users/:userId/accounts", this::getAccounts);

        post("/transfers", this::transfer);
    }

    private Object addAccount(Request request, Response response) throws IOException {
        final Account account = mapper.readValue(request.body(), Account.class);
        if (account.getId() != 0) {
            // has to be 0, as create new entity
            halt(400);
            return null;
        }
        String stringUserId = request.params("userId");
        if (stringUserId == null) {
            halt(400);
            return null;
        }
        User user = databaseService.getUser(Long.valueOf(stringUserId));
        if (user == null) {
            halt(404);
            return null;
        }
        account.setUser(user);
        user.getAccount().add(account);

        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();

            session.save(account);
            session.update(user);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            response.status(500);
        }

        response.status(201);
        response.header("content-type", "application/json; charset=utf-8");
        return mapper.writeValueAsString(account);
    }

    public Object getAccounts(Request request, Response response) throws IOException {
        String stringUserId = request.params("userId");
        if (stringUserId == null) {
            halt(400);
            return null;
        }
        Long userId = Long.valueOf(stringUserId);
        User user = databaseService.getUser(userId);
        if (user == null) {
            halt(404);
            return null;
        }
        return mapper.writeValueAsString(user.getAccount());
    }

    private Object addUser(Request request, Response response) throws IOException {
        final User user = mapper.readValue(request.body(), User.class);
        if (user.getId() != 0) {
            // has to be 0, as create new entity
            halt(400);
            return null;
        }
        databaseService.saveUser(user);
        response.status(201);
        response.header("content-type", "application/json; charset=utf-8");
        return mapper.writeValueAsString(user);
    }

    private Object getUser(Request request, Response response) throws JsonProcessingException {
        String stringUserId = request.params("userId");
        if (stringUserId == null) {
            halt(400);
            return null;
        }
        Long userId = Long.valueOf(stringUserId);
        User user = databaseService.getUser(userId);
        if (user == null) {
            halt(404);
            return null;
        }
        response.header("content-type", "application/json; charset=utf-8");
        return mapper.writeValueAsString(user);
    }

    private Object transfer(Request request, Response response) throws IOException {
        Transaction transaction = mapper.readValue(request.body(), Transaction.class);

        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();

            Account from = session.get(Account.class, transaction.getFromAccountId(), LockMode.PESSIMISTIC_WRITE);
            Account to = session.get(Account.class, transaction.getToAccountId(), LockMode.PESSIMISTIC_WRITE);

            if (from.getAmmount() >= transaction.getSum()) {
                from.setAmmount(from.getAmmount() - transaction.getSum());
                to.setAmmount(to.getAmmount() + transaction.getSum());
            } else {
                session.getTransaction().rollback();
                response.status(400);
                return "Not enough money.";
            }

            session.getTransaction().commit();
            return "Successful transfer from account #" + transaction.getFromAccountId() + " to #" + transaction.getToAccountId();
        } catch (Exception e) {
            session.getTransaction().rollback();
            halt(500);
            return null;
        } finally {
            session.close();
        }

    }

}

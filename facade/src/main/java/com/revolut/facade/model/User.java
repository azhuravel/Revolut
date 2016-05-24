package com.revolut.facade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by azhuravel on 19.05.16.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToMany
    private Collection<Account> account;

    public User() {
    }

    public User(long id, String name, Collection<Account> account) {
        this.id = id;
        this.name = name;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Collection<Account> getAccount() {
        return account;
    }

    public void setAccount(Collection<Account> account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.lama;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "people")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private boolean logged=false;

    public Person(String name, String password) {
        this.name = name;
        this.password = password;
        this.logged = false;
    }

    public Person() {
        this.logged = false;
    }

    public abstract String accountType();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}

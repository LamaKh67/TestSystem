package com.lama;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "managerID")
@NoArgsConstructor
public class Manager extends Person implements Serializable {
    private static Manager instance;

    @Transient
    public List<Subject> subjects = new LinkedList<Subject>();

    private Manager(String name, String password) {
        super(name, password);
    }

    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager("Malki Grossman", "malki");
        }
        return instance;
    }

    public String accountType() {
        return "manager";
    }
}

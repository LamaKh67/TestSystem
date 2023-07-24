package com.lama;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name="managerID")
public class Manager extends Person implements Serializable {

    @Transient
    public List<Subject> subjects = new LinkedList<Subject>();

    public Manager(String name, String password) {
        super(name, password);
    }

    public Manager() {
        super();
    }

    public String accountType(){
        return "manager";
    }
}

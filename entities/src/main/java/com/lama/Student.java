package com.lama;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name="studentID")
public class Student extends Person implements Serializable {
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    public Student(String id, String name, String password) {
        super(name, password);
    }

    public Student() {
        super();
    }

    public String accountType(){
        return "student";
    }
}

package com.lama;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name="studentID")
public class Student extends Person implements Serializable {

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "students")
    private List<Course> courses;

    public Student(String name, String password) {
        super(name, password);
        this.courses = new LinkedList<Course>();
    }

    public Student() {
        super();
    }

    public List<Course> getCourses(){
        return this.courses;
    }

    public void addCourse(Course course){
        this.courses.add(course);
    }

    public String accountType(){
        return "student";
    }
}

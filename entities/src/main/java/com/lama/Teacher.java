package com.lama;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name="teacherID")
public class Teacher extends Person implements Serializable {
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, mappedBy = "course_teacher")
    private List<Course> courses;

    public Teacher(String name, String password) {
        super(name, password);
        this.courses = new LinkedList<Course>();
    }

    public Teacher() {
        super();
    }

    public void addCourse(Course course){
        this.courses.add(course);
    }

    public String accountType(){
        return "teacher";
    }

    public List<Course> getCourses(){
        return courses;
    }
}

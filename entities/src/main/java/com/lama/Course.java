package com.lama;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_subject_id")
    private Subject course_subject;

    @ManyToOne
    @JoinColumn(name = "course_teacher_id")
    private Teacher course_teacher;

    @ManyToMany(mappedBy = "courses")
    private List<Question> questions;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;

    public Course(String name, Subject course_subject) {
        this.name = name;
        this.course_subject = course_subject;
        this.questions = new LinkedList<Question>();
        this.students = new LinkedList<Student>();
    }

    public Course() {

    }

    public void setCourse_teacher(Teacher teacher){
        this.course_teacher = teacher;
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getCourse_subject() {
        return course_subject;
    }

    public void setCourse_subject(Subject course_subject) {
        this.course_subject = course_subject;
    }
}

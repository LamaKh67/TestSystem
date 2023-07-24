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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "courses")
    private List<Question> questions;

    @ManyToMany
    @JoinTable(
            name = "courses_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
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

    public void addStudent(Student student){
        this.students.add(student);
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

    public List<Question> getQuestions() {
        return questions;
    }
}

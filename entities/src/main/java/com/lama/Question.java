package com.lama;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(length = 5)
    private String question_number;

    @ManyToMany
    @JoinTable(
            name = "courses_questions",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    private String question;

    private String instructions;

    private String answer1;

    private String answer2;

    private String answer3;

    private String answer4;

    private final int correct_answer;

    public Question(String question, String question_number, String instructions, String answer1, String answer2, String answer3, String answer4, int correctAnswer) {
        this.question = question;
        this.question_number = question_number;
        this.instructions = instructions;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        correct_answer = correctAnswer;
        this.courses = new LinkedList<Course>();
    }

    public Question() {
        correct_answer = 1;
    }

    public String getQuestion_number() {
        return question_number;
    }

    public void setQuestion_number(String question_number) {
        this.question_number = question_number;
    }

    public int getId() {
        return id;
    }

    public void addCourse(Course course){
        courses.add(course);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getCorrect_answer() {
        return correct_answer;
    }
}

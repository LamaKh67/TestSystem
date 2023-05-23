package com.lama;

import java.util.List;
import java.util.Random;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.jaxb.hbm.spi.SubEntityInfo;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
public class App
{

    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Course.class);
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Question.class);
        configuration.addAnnotatedClass(Subject.class);
        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(Teacher.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void generateData() throws Exception {
        Subject subject = new Subject("Subject Name");

        Course course1 = new Course("Course 1", subject);
        Course course2 = new Course("Course 2", subject);
        Course course3 = new Course("Course 3", subject);

        Question question1 = new Question("Question 1", "23578", "Instructions 1", "Answer 1.1", "Answer 1.2", "Answer 1.3", "Answer 1.4", 1);
        Question question2 = new Question("Question 2","23518", "Instructions 2", "Answer 2.1", "Answer 2.2", "Answer 2.3", "Answer 2.4", 2);
        Question question3 = new Question("Question 3", "23078", "Instructions 3", "Answer 3.1", "Answer 3.2", "Answer 3.3", "Answer 3.4", 3);
        subject.addCourse(course1);
        subject.addCourse(course2);
        subject.addCourse(course3);
        course1.addQuestion(question1);
        question1.addCourse(course1);
        course1.addQuestion(question2);
        question2.addCourse(course1);
        course1.addQuestion(question3);
        question3.addCourse(course1);

        course2.addQuestion(question1);
        question1.addCourse(course2);
        course2.addQuestion(question2);
        question2.addCourse(course2);
        course2.addQuestion(question3);
        question3.addCourse(course2);

        course3.addQuestion(question1);
        question1.addCourse(course3);
        course3.addQuestion(question2);
        question2.addCourse(course3);
        course3.addQuestion(question3);
        question3.addCourse(course3);

        Teacher teacher1 = new Teacher("Teacher 1 ID", "Teacher 1 Name", "Teacher 1 Password");
        Teacher teacher2 = new Teacher("Teacher 2 ID", "Teacher 2 Name", "Teacher 2 Password");
        Teacher teacher3 = new Teacher("Teacher 3 ID", "Teacher 3 Name", "Teacher 3 Password");
        course1.setCourse_teacher(teacher1);
        course2.setCourse_teacher(teacher2);
        course3.setCourse_teacher(teacher3);

        session.save(subject);

        session.save(course1);
        session.save(course2);
        session.save(course3);

        session.save(question1);
        session.save(question2);
        session.save(question3);

        session.save(teacher1);
        session.save(teacher2);
        session.save(teacher3);




//        Random random = new Random();
//
//        Subject subject = new Subject("Computer Science");
//        String[] courses = {"Intro to computer science", "Discrete Mathematics", "Algebra A"};
//        Course[] courses_list = new Course[3];
//        session.save(subject);
//
//        for (int i = 0; i < 3; i++) {
//            Course course = new Course(courses[i], subject);
//            course.setCourse_subject(subject);
//            subject.addCourse(course);
//            courses_list[i] = course;
//            session.save(course);
//        }
//
//        String[] questions = {"What is the time complexity of merge sort?", "Choose the closest O notation", "O(n)",
//        "O(n^2)", "O(nlogn)", "O(nloglogn)", "Which data structure follows the Last-In-First-Out (LIFO) principle?",
//                "There's exactly one correct answer!", "Queue", "Stack", "Linked List", "Array",
//                "What is the purpose of a compiler in programming?", "There's exactly one correct answer!",
//                    "To execute the program", "To debug the program", "To provide a user interface", "To convert the program into machine code"};
//        Integer[] answers = {3, 2, 4, 2, 1, 1, 3, 1, 4};
//        Question[] questions_list = new Question[9];
//        for (int i = 0; i < 9; i++) {
//            if(i < 3) {
//                Question question = new Question(questions[i * 5], questions[i * 5 + 1], questions[i * 5 + 2],
//                        questions[i * 5 + 3], questions[i * 5 + 4], questions[i * 5 + 5], answers[i]);
//                question.addCourse(courses_list[i]);
//                courses_list[i].addQuestion(question);
//                questions_list[i] = question;
//                session.save(question);
//            }else{
//                Question question = new Question("a" + Integer.toString(i), "ll", "a",
//                        "aa", "aaa", "aaaa", answers[i]);
//                question.addCourse(courses_list[1]);
//                courses_list[1].addQuestion(question);
//                questions_list[i] = question;
//                session.save(question);
//            }
//        }
//
//        Teacher teacher1 = new Teacher("123456789","Lama", "lamsha");
//        Teacher teacher2 = new Teacher("123457589","Shira", "123");
//
//        teacher1.addCourse(courses_list[0]);
//        teacher2.addCourse(courses_list[1]);
//        teacher2.addCourse(courses_list[2]);
//
//        session.save(teacher1);
//        session.save(teacher2);
//        session.flush();
    }
    public static void main( String[] args ) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();


            generateData();

            session.getTransaction().commit(); // Save everything.

        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
        }
    }
}

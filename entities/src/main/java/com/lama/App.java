package com.lama;

import java.util.List;
import java.util.Random;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
        configuration.addAnnotatedClass(Manager.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static <T> List<T> getAll(Class<T> object) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(object);
        Root<T> rootEntry = criteriaQuery.from(object);
        CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);

        TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
        return allQuery.getResultList();
    }

    private static void generateData() throws Exception {
        Subject subject = new Subject("Computer Science");

        Course course1 = new Course("Intro to computer science", subject);
        Course course2 = new Course("Data Structures", subject);
        Course course3 = new Course("OOP", subject);
        Course course4 = new Course("Hardware", subject);
        Course course5 = new Course("Operating Systems", subject);

        Question question1 = new Question("What is the time complexity of merge sort?", "01001", "Choose the closest O notation", "O(n)", "O(n^2)", "O(nlogn)", "O(nloglogn)", 3);
        Question question2 = new Question("Which data structure follows the Last-In-First-Out (LIFO) principle?", "01002",
                "There's exactly one correct answer!", "Queue", "Stack", "Linked List", "Array", 2);
        Question question3 = new Question("What is the purpose of a compiler in programming?", "01003", "There's exactly one correct answer!",
                    "To execute the program", "To debug the program", "To provide a user interface", "To convert the program into machine code", 4);
        Question question4 = new Question("Which of the following is NOT a type of operating system kernel?", "01004",
                "There's exactly one correct answer!", "Monolithic kernel", "Microkernel", "Hybrid kernel", "Exokernel", 4);
        subject.addCourse(course1);
        subject.addCourse(course2);
        subject.addCourse(course3);
        subject.addCourse(course4);
        subject.addCourse(course5);
        course1.addQuestion(question1);
        question1.addCourse(course1);
        course1.addQuestion(question2);
        question2.addCourse(course1);
        course1.addQuestion(question3);
        question3.addCourse(course1);

        course2.addQuestion(question1);
        question1.addCourse(course2);

        course3.addQuestion(question1);
        question1.addCourse(course3);
        course3.addQuestion(question2);
        question2.addCourse(course3);
        course3.addQuestion(question3);
        question3.addCourse(course3);

        course4.addQuestion(question4);
        question4.addCourse(course4);
        course5.addQuestion(question4);
        question4.addCourse(course5);

        Teacher teacher1 = new Teacher("Yael", "pass1");
        Teacher teacher2 = new Teacher("Shir Avneri", "pass2");
        Teacher teacher3 = new Teacher("Lama Khalaily", "pass3");
        Teacher teacher4 = new Teacher("t1", "t1");
        course1.setCourse_teacher(teacher1);
        course2.setCourse_teacher(teacher2);
        course3.setCourse_teacher(teacher3);
        course4.setCourse_teacher(teacher4);
        course5.setCourse_teacher(teacher4);

        Manager manager = Manager.getInstance();

        session.save(subject);

        session.save(course1);
        session.save(course2);
        session.save(course3);
        session.save(course4);
        session.save(course5);

        session.save(question1);
        session.save(question2);
        session.save(question3);
        session.save(question4);

        session.save(teacher1);
        session.save(teacher2);
        session.save(teacher3);
        session.save(teacher4);

        session.save(manager);

    }

    private static void generateData2() throws Exception {
        Subject subject = new Subject("Physics");

        Course course1 = new Course("Classical Mechanics", subject);
        Course course2 = new Course("Quantum Mechanics", subject);
        Course course3 = new Course("Nuclear Physics", subject);
        Course course4 = new Course("Solid State Physics", subject);

        Question question1 = new Question("A projectile is launched at an angle from the ground. How does the range of the projectile depend on the launch angle and initial velocity?",
                "02001", "There's exactly one correct answer!", "The range is independent of both the launch angle and initial velocity.",
                "The range depends only on the launch angle.", "The range depends only on the initial velocity.",
                "The range depends on both the launch angle and initial velocity.", 3);

        Question question2 = new Question("x?", "02002",
                "There's exactly one correct answer!", "a", "b", "c", "d", 2);
        Question question3 = new Question("y?", "02003", "There's exactly one correct answer!",
                "aa", "bb", "cc", "dd", 4);
        Question question4 = new Question("z?", "02004", "There's exactly one correct answer!",
                "kk1", "kk2", "kk3", "kk4", 3);
        subject.addCourse(course1);
        subject.addCourse(course2);
        subject.addCourse(course3);
        subject.addCourse(course4);
        course1.addQuestion(question1);
        question1.addCourse(course1);
        course2.addQuestion(question2);
        question2.addCourse(course2);
        course3.addQuestion(question3);
        question3.addCourse(course3);
        course4.addQuestion(question4);
        question4.addCourse(course4);

        course2.addQuestion(question1);
        question1.addCourse(course2);

        Teacher teacher1 = new Teacher("Bill", "bill");
        Teacher teacher2 = new Teacher("Steve", "steve");
        Teacher teacher3 = new Teacher("Anna", "anna");
        course1.setCourse_teacher(teacher1);
        course2.setCourse_teacher(teacher2);
        course3.setCourse_teacher(teacher3);
        course4.setCourse_teacher(teacher3);

        session.save(subject);

        session.save(course1);
        session.save(course2);
        session.save(course3);

        session.save(question1);
        session.save(question2);
        session.save(question3);
        session.save(question4);

        session.save(teacher1);
        session.save(teacher2);
        session.save(teacher3);
        session.flush();
    }

    private static void generateData3() throws Exception{
        List<Course> courses = getAll(Course.class);
        Student student1 = new Student("student1", "st1");
        Student student2 = new Student("student2", "st2");

        student1.addCourse(courses.get(0));
        courses.get(0).addStudent(student1);
        student1.addCourse(courses.get(2));
        courses.get(2).addStudent(student1);
        student1.addCourse(courses.get(3));
        courses.get(3).addStudent(student1);

        student2.addCourse(courses.get(1));
        courses.get(1).addStudent(student2);
        student2.addCourse(courses.get(2));
        courses.get(2).addStudent(student2);

        session.save(student1);
        session.save(student2);

        for(Course course:courses) {
            session.update(course);
        }
        session.flush();
    }

    private static void generateData4() throws Exception {
        Subject subject = new Subject("Mathematics");

        Course course1 = new Course("Infi-1", subject);
        Course course2 = new Course("Infi-2", subject);
        Course course3 = new Course("Algebra", subject);
        Course course4 = new Course("Statistics", subject);

        Question question1 = new Question("A projectile is launched at an angle from the ground. How does the range of the projectile depend on the launch angle and initial velocity?",
                "03001", "There's exactly one correct answer!", "The range is independent of both the launch angle and initial velocity.",
                "The range depends only on the launch angle.", "The range depends only on the initial velocity.",
                "The range depends on both the launch angle and initial velocity.", 3);
        Question question2 = new Question("x?", "03002",
                "There's exactly one correct answer!", "a", "b", "c", "d", 2);
        Question question3 = new Question("y?", "03003", "There's exactly one correct answer!",
                "aa", "bb", "cc", "dd", 4);
        Question question4 = new Question("z?", "03004", "There's exactly one correct answer!",
                "kk1", "kk2", "kk3", "kk4", 3);
        subject.addCourse(course1);
        subject.addCourse(course2);
        subject.addCourse(course3);
        subject.addCourse(course4);
        course1.addQuestion(question1);
        question1.addCourse(course1);
        course2.addQuestion(question2);
        question2.addCourse(course2);
        course3.addQuestion(question3);
        question3.addCourse(course3);
        course4.addQuestion(question4);
        question4.addCourse(course4);

        course2.addQuestion(question1);
        question1.addCourse(course2);

        Teacher teacher1 = new Teacher("t2", "t2");
        Teacher teacher2 = new Teacher("t3", "t3");
        Teacher teacher3 = new Teacher("t4", "t4");
        course1.setCourse_teacher(teacher1);
        course2.setCourse_teacher(teacher2);
        course3.setCourse_teacher(teacher3);
        course4.setCourse_teacher(teacher3);

        session.save(subject);

        session.save(course1);
        session.save(course2);
        session.save(course3);

        session.save(question1);
        session.save(question2);
        session.save(question3);
        session.save(question4);

        session.save(teacher1);
        session.save(teacher2);
        session.save(teacher3);
        session.flush();
    }

    public static void main( String[] args ) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();


            generateData();
            generateData2();
            generateData3();

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

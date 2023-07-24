package com.lama;

import com.lama.Message;
import com.lama.ocsf.AbstractServer;
import com.lama.ocsf.ConnectionToClient;
import com.lama.ocsf.SubscribedClient;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleServer extends AbstractServer {
	private static SessionFactory sessionFactory;
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	public SimpleServer(int port) {
		super(port);
		sessionFactory=getSessionFactory();
	}

	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Course.class);
		configuration.addAnnotatedClass(Person.class);
		configuration.addAnnotatedClass(Question.class);
		configuration.addAnnotatedClass(Subject.class);
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Teacher.class);
		configuration.addAnnotatedClass(Manager.class);
		ServiceRegistry serviceRegistry = (new StandardServiceRegistryBuilder())
				.applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(serviceRegistry);
	}

	public static <T> List<T> getAll(Class<T> object, Session session) throws Exception {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(object);
		Root<T> rootEntry = criteriaQuery.from(object);
		CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);

		TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
		return allQuery.getResultList();
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;
		String request = message.getMessage();
		System.out.println(request);
		Session newsession = sessionFactory.openSession();
		Transaction tx=null;
		try {
			tx=newsession.beginTransaction();
			//we got an empty message, so we will send back an error message with the error details.
			if (request.isBlank()) {
				message.setMessage("Error! we got an empty message");
				client.sendToClient(message);
			}
			else if(request.startsWith("SignIn#")){
				List<Teacher> teachers = null;
				List<Student> students = null;
				List<Manager> managers = null;
				String[] details = request.split("#");
				int id = Integer.parseInt(details[1]);
				String password = details[2];
				boolean found = false;
				teachers = getAll(Teacher.class, newsession);
				for(Teacher user:teachers){
					if (user.getId() == id && user.getPassword().equals(password)){
						if(user.isLogged()){
							message.setMessage("User already signed in!");
							client.sendToClient(message);
						}else {
							user.setLogged(true);
							newsession.update(user);
							newsession.flush();
							client.sendToClient(user);
						}
						found = true;
						break;
					}
				}
				students = getAll(Student.class, newsession);
				for(Student user:students) {
					if (user.getId() == id && user.getPassword().equals(password)) {
						if(user.isLogged()){
							message.setMessage("User already signed in!");
							client.sendToClient(message);
						}else {
							user.setLogged(true);
							newsession.update(user);
							newsession.flush();
							client.sendToClient(user);
						}
						found = true;
						break;
					}
				}
				managers = getAll(Manager.class, newsession);
				for(Manager user:managers) {
					if (user.getId() == id && user.getPassword().equals(password)) {
                        List<Subject> subjects = getAll(Subject.class, newsession);
                        for(Subject subject:subjects){
                            user.subjects.add(subject);
                        }
						if(user.isLogged()){
							message.setMessage("User already signed in!");
							client.sendToClient(message);
						}else {
							user.setLogged(true);
							newsession.update(user);
							newsession.flush();
							client.sendToClient(user);
						}
						found = true;
						break;
					}
				}
				if(!found){
					client.sendToClient(new Message(55, "ID or password are incorrect."));
				}
			}
			else if(request.startsWith("signOut#")){
				List<Person> people = null;
				String[] content = request.split("#");
				int id = Integer.parseInt(content[1]);

				people = getAll(Person.class, newsession);

				for(Person user:people){
					if(user.getId() == id){
						user.setLogged(false);
						newsession.update(user);
						newsession.flush();
					}
				}
			}
			else if(request.startsWith("showQuestion:")){
				String question_number = request.substring(13);
				List<Question> questionList = getAll(Question.class, newsession);
				System.out.println(question_number);

				for(Question question:questionList){
					if(question.getQuestion_number().equals(question_number)) {
						client.sendToClient(question);
					}
				}
			}
			//we got a request to add a new client as a subscriber.
			else if (request.equals("getQuestionNumbers")){
				List<Question> questionList = getAll(Question.class, newsession);
				StringBuffer questions = new StringBuffer();

				for(Question question:questionList){
					questions.append(question.getQuestion_number());
					questions.append('#');
				}
				message.setMessage(questions.toString());
				client.sendToClient(message);
			}else if (request.startsWith("update_question:")){
				String question_number = request.substring(16, 21);
				List<Question> questionList = getAll(Question.class, newsession);
				String[] contents = request.split("#");

				for (int i = 0; i < questionList.size(); i++){
					if(question_number.equals(questionList.get(i).getQuestion_number())){
						questionList.get(i).setQuestion(contents[1]);
						questionList.get(i).setInstructions(contents[2]);
						questionList.get(i).setAnswer1(contents[3]);
						questionList.get(i).setAnswer2(contents[4]);
						questionList.get(i).setAnswer3(contents[5]);
						questionList.get(i).setAnswer4(contents[6]);
					}
				}

				for (Question question : questionList) {
					newsession.update(question);
					newsession.flush();
				}

				for (Question question : questionList) {
					if(question_number.equals(question.getQuestion_number()))
						sendToAllClients(question);
				}
			}
			else if(request.startsWith("add_question:")){
				String[] contents = request.split("#");
				Question question = new Question(contents[2], contents[1], contents[3], contents[4], contents[5], contents[6],
						contents[7], Integer.parseInt(contents[8]));
				List<Course> courses = getAll(Course.class, newsession);
				for(int i = 9; i < contents.length; i++){
					for(Course course:courses){
						if(course.getId() == Integer.parseInt(contents[i])){
							System.out.println(course.getId());
							course.addQuestion(question);
							question.addCourse(course);
							newsession.update(course);
						}
					}
				}
				newsession.save(question);
				newsession.flush();
			}
			else if(request.startsWith("getSubjectHistogram#")){
				String[] contents = request.split("#");
				int id = Integer.parseInt(contents[1]);
				Message message1 = new Message(117, "getSubjectHistogram");
				List<Course> courses = getAll(Course.class, newsession);
				for(Course course:courses){
					if(course.getCourse_subject().getId() == id)
						message1.hist.put(course.getName(), course.getQuestions().size());
				}
				client.sendToClient(message1);
			}
			else if(request.equals("get_subjects")){
				List<Subject> subjects = getAll(Subject.class, newsession);
				Message message1 = new Message(200, "get_subjects");
				for(Subject subject:subjects){
					message1.objects.add(subject);
				}
				client.sendToClient(message1);
			}
			//we got a message from client requesting to echo Hello, so we will send back to client Hello world!
			else if(request.startsWith("correct_answers:#")){
				String[] ids = request.split("#");
				String str = "";
				List<Question> questions = getAll(Question.class, newsession);
				for(int i = 1; i < ids.length; i++){
					int id = Integer.parseInt(ids[i]);
					for(Question question:questions){
						if(question.getId() == id){
							str += question.getCorrect_answer() + "#";
							break;
						}
					}
				}
				Message message1 = new Message(206, str);
				client.sendToClient(message1);
			}
			else if(request.startsWith("send Submitters IDs")){
				//add code here to send submitters IDs to client
			}
			else if (request.startsWith("send Submitters")){
				//add code here to send submitters names to client
			}
			else if (request.equals("what’s the time?")) {
				//add code here to send the time to client
			}
			else if (request.startsWith("multiply")){
				//add code here to multiply 2 numbers received in the message and send result back to client
				//(use substring method as shown above)
				//message format: "multiply n*m"
			}else{
				//add code here to send received message to all clients.
				//The string we received in the message is the message we will send back to all clients subscribed.
				//Example:
					// message received: "Good morning"
					// message sent: "Good morning"
				//see code for changing submitters IDs for help
				System.out.println(request);
			}
			tx.commit();
		} catch (Exception var10) {
			if (newsession != null) {
				newsession.getTransaction().rollback();
			}

			System.err.println("An error occured, changes have been rolled back.");
			var10.printStackTrace();
		} finally {
			if (newsession != null) {
				newsession.close();
			}

		}
	}

	public void sendToAllClients(Message message) {
		try {
			for (SubscribedClient SubscribedClient : SubscribersList) {
				SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

package com.lama;

import com.lama.Message;
import org.greenrobot.eventbus.EventBus;

import com.lama.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if(msg.getClass().equals(Message.class)){
			Message message = (Message) msg;
			if(message.getMessage().equals("Error! we got an empty message")) {
				EventBus.getDefault().post(new ErrorEvent(message));
			}else{
				System.out.println(message.getMessage());
				EventBus.getDefault().post(message);
			}
		}else if(msg.getClass().equals(Question.class)){
			EventBus.getDefault().post((Question)msg);
		}else if(msg.getClass().equals(Teacher.class)){
			EventBus.getDefault().post((Teacher)msg);
		}else if(msg.getClass().equals(Student.class)){
			EventBus.getDefault().post((Student)msg);
		}else if(msg.getClass().equals(Manager.class)){
			EventBus.getDefault().post((Manager)msg);
		}
		else{
			System.out.println(msg);
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}

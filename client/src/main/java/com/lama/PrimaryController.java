package com.lama;


import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.lama.Message;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import static com.lama.SimpleClient.getClient;

public class PrimaryController {
	@FXML
	private Button signinBtn;

	@FXML
	private TextField idTxt;

	@FXML
	private PasswordField passwordTxt;

	@FXML
	private ImageView img1;

	@FXML
	private ImageView img2;

	@FXML
	private ImageView img3;

	@FXML
	private ImageView img4;

	@FXML
	private Label passLbl1;

	@FXML
	private Label passLbl2;

	@FXML
	private TextField timeTF;

	private int msgId;

	@FXML
	private Label wrongLbl;

	@FXML
	void ButtonDragOP(MouseEvent event) {

	}

	@FXML
	void ButtonUnDragOP(MouseEvent event) {

	}

	@FXML
	void DisclaimerOP(ActionEvent event) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);

			Text title = new Text("~By logging in to TestSystem, I agree and confirm that:");
			title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

			Text message = new Text("I know that TestSystem (like other computer systems) documents all the activities that take place within it,\n" +
					"including: viewing study materials, submitting tests, and more.\n" +
					"I know and accept that my session activity will be stored in the TestSystem, will be available to the\n" +
					"teaching staff and school representatives and will be used to monitor the system's activities and\n" +
					"manage learning processes, including checking my compliance with the course(s).");
			message.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

			VBox content = new VBox(title, message);
			alert.getDialogPane().setContent(content);

			alert.setHeaderText("Disclaimer");
			alert.setTitle("Disclaimer");
			alert.show();
		});
	}

	@FXML
	void SignInOP(ActionEvent event) throws IOException {
		if(!EventBus.getDefault().isRegistered(this))
			EventBus.getDefault().register(this);
		getClient().sendToServer(new Message(5, "SignIn#"
				+ idTxt.getText() + "#" + passwordTxt.getText()));
	}

	@Subscribe
	public void onMessage(Message message){
		Platform.runLater(() -> {
			if(message.getMessage().equals("User already signed in!"))
				wrongLbl.setText(message.getMessage());
			else if(message.getMessage().equals("ID or password are incorrect."))
				wrongLbl.setText(message.getMessage());
			wrongLbl.setVisible(true);
			if (EventBus.getDefault().isRegistered(this))
				EventBus.getDefault().unregister(this);
		});
	}

	@Subscribe
	public void openPage(Person user) {
		Platform.runLater(() -> {
			if(EventBus.getDefault().isRegistered(this))
				EventBus.getDefault().unregister(this);
			if (user instanceof Teacher) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				SecondaryController page = loader.getController();
				page.setData((Teacher) user);

				Scene newscene = new Scene(root);
				if(EventBus.getDefault().isRegistered(this))
					EventBus.getDefault().unregister(this);
				SimpleChatClient.stage.setScene(newscene);
				SimpleChatClient.stage.setMaximized(true);
				SimpleChatClient.stage.show();
			}
			else if (user instanceof Student) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("student.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				StudentController page = loader.getController();
				page.setData((Student) user);

				Scene newscene = new Scene(root);
				if(EventBus.getDefault().isRegistered(this))
					EventBus.getDefault().unregister(this);
				SimpleChatClient.stage.setScene(newscene);
				SimpleChatClient.stage.setMaximized(true);
				SimpleChatClient.stage.show();
			}
			else if (user instanceof Manager) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("manager.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				ManagerController page = loader.getController();
				page.setData((Manager) user);

				Scene newscene = new Scene(root);
				if(EventBus.getDefault().isRegistered(this))
					EventBus.getDefault().unregister(this);
				SimpleChatClient.stage.setScene(newscene);
				SimpleChatClient.stage.setMaximized(true);
				SimpleChatClient.stage.show();
			}
		});
	}

	@Subscribe
	public void errorEvent(ErrorEvent event){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR,
					String.format("Message:\nId: %d\nData: %s\nTimestamp: %s\n",
							event.getMessage().getId(),
							event.getMessage().getMessage(),
							event.getMessage().getTimeStamp().format(dtf))
			);
			alert.setTitle("Error!");
			alert.setHeaderText("Error:");
			alert.show();
		});
	}

	@FXML
	void initialize() {
		msgId=0;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime startTime = LocalTime.now().plusHours(1); // Start from 1 hour from now
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalTime currentTime = LocalTime.now();
			long secondsRemaining = ChronoUnit.SECONDS.between(currentTime, startTime);
			if (secondsRemaining >= 0) { // Only update if countdown hasn't finished
				LocalTime displayTime = LocalTime.ofSecondOfDay(secondsRemaining);
				timeTF.setText(displayTime.format(dtf));
			}
		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}
}
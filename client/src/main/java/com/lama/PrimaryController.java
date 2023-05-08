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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PrimaryController {
	@FXML
	private Button signinBtn;

	@FXML
	private TextField idTxt;

	@FXML
	private TextField passwordTxt;

	@FXML
	private ComboBox<?> typeField;

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
		System.out.println("ghgj");
		SimpleClient.getClient().sendToServer(new Message(5, "sfkgkd"));
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
		EventBus.getDefault().register(this);
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
		System.out.println("sdf");
	}
}
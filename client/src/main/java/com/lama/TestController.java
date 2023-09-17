package com.lama;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.sql.Struct;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class TestController {

    @FXML
    private GridPane questionsGrid;

    @FXML
    private TextField timeTF;

    @FXML
    private Label errorLbl;

    @FXML
    private Label gradeLbl;

    private boolean timeFinished = false;

    private boolean submitted = false;

    private Course course;

    private List<Question> questions = null;

    private List<QuestionController> questionControllers = new LinkedList<QuestionController>();

    private String questions_ids = "";

    public void setData(Course course){
        this.course = course;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startTime = LocalTime.now().plusMinutes(2);
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            long secondsRemaining = ChronoUnit.SECONDS.between(currentTime, startTime);
            if (secondsRemaining >= 0) { // Only update if countdown hasn't finished
                LocalTime displayTime = LocalTime.ofSecondOfDay(secondsRemaining);
                timeTF.setText(displayTime.format(dtf));
            }else if(!timeFinished){
                try {
                    timeFinished = true;
                    submitOP(new ActionEvent());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        questions = course.getQuestions();
        int column=0, row=1;
        questionsGrid.getChildren().clear();
        for(Question question: questions){
            questions_ids += Integer.toString(question.getId()) + "#";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(this.getClass().getResource("question.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = (AnchorPane) fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            QuestionController itemController = fxmlLoader.getController();
            itemController.setData(question);
            itemController.isTest();
            questionControllers.add(itemController);

            questionsGrid.add(anchorPane, column, row);
            //set grid width
            questionsGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
            questionsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            questionsGrid.setMaxWidth(Region.USE_PREF_SIZE);

            //set grid height
            questionsGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
            questionsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            questionsGrid.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(anchorPane, new Insets(0, 0, 10, 0));
            row++;
        }
    }

    @FXML
    void submitOP(ActionEvent event) throws IOException {
        if(submitted)
            return;
        submitted = true;

        errorLbl.setVisible(false);
        if(!timeFinished) {
            for (QuestionController questionController : questionControllers) {
                if (questionController.chosenAnswer() == 5) {
                    errorLbl.setVisible(true);
                    submitted = false;
                }
            }
        }
        if(!errorLbl.isVisible()) {
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
            SimpleClient.getClient().sendToServer(new Message(205, "correct_answers:#" + questions_ids));
        }
    }

    @Subscribe
    public void showAnswers(Message message){
        Platform.runLater(() -> {
            if(message.getMessage().equals("refresh")){
                return;
            }
            String[] answers = message.getMessage().split("#");
            int i = 0, correct = 0;
            for (QuestionController questionController : questionControllers) {
                int answer = Integer.parseInt(answers[i]);
                int chosen = questionController.chosenAnswer();

                questionController.setCorrectAnswer(answer);
                if (answer != chosen) {
                    questionController.setWrongAnswer(chosen);
                } else {
                    correct++;
                }
                i++;
            }
            gradeLbl.setText(gradeLbl.getText() + Integer.toString(correct) + "/" + Integer.toString(questions.size()));
            gradeLbl.setVisible(true);

            if (EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().unregister(this);
        });
    }
}

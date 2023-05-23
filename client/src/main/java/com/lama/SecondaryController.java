package com.lama;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.LinkedList;

public class SecondaryController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane questionsGrid;

    @FXML
    private GridPane questionNumbers;

    int message_id = 10;

    @FXML
    void initialize() {
    }

    @FXML
    void showQuestions(ActionEvent event) throws IOException {
        EventBus.getDefault().register(this);
        SimpleClient.getClient().sendToServer(new Message(message_id++, "getQuestionNumbers"));
    }

    @Subscribe
    public void onShowQuestionsNumbers(Message numbers){
        System.out.println(numbers.getMessage());
        Platform.runLater(() -> {
        String str = numbers.getMessage();
        String[] question_numbers = str.split("#");

        Button[] buttons = new Button[question_numbers.length];
        int row=0;
        for(int i = 0; i < question_numbers.length; i++){
            row++;
            buttons[i] = new Button();
            buttons[i].setText(question_numbers[i]);
            buttons[i].setStyle("-fx-font-weight: bold;");
            buttons[i].setMinWidth(180);
            questionNumbers.add(buttons[i], 0, row);

            int finalI = i;
            buttons[i].setOnAction(e -> {
                try {
                    SimpleClient.getClient().sendToServer(new Message(message_id++, "showQuestion:" + question_numbers[finalI]));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

        }
        });
    }

    @Subscribe
    public void onShowQuestionsEvent(Question question) {
        System.out.println("aa1");
        Platform.runLater(() -> {
        questionsGrid.getChildren().clear();
        int column=0, row=1;

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
        });
    }
}

package com.lama;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class QuestionController {

    private Question question;

    @FXML
    private Label questionTxt;

    @FXML
    private Label questionInst;

    @FXML
    private CheckBox answer1;

    @FXML
    private CheckBox answer2;

    @FXML
    private CheckBox answer3;

    @FXML
    private CheckBox answer4;

    public void setData(Question question){
        this.question = question;

        questionTxt.setText(question.getQuestion());
        questionInst.setText(question.getInstructions());
        answer1.setText(question.getAnswer1());
        answer2.setText(question.getAnswer2());
        answer3.setText(question.getAnswer3());
        answer4.setText(question.getAnswer4());
    }

    @FXML
    void updateQuestion(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("question_create.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Question_createController itemController = fxmlLoader.getController();
        itemController.setData(question.getQuestion(), question.getInstructions(), question.getAnswer1(), question.getAnswer2(),
                question.getAnswer3(), question.getAnswer4(), question.getCorrect_answer(), question.getQuestion_number());
        Stage stage = new Stage();
// Setting the title and the icon behind the title.
        stage.setTitle("TestSystem");
        URL url = getClass().getResource("/images/icon.png");
        String path = url.toExternalForm();
        Image icon = new Image(path);
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        stage.getIcons().add(icon);
        Label titleLabel = new Label("Title");
        titleLabel.setGraphic(imageView);
        stage.setScene(scene);
        stage.show();

    }
}

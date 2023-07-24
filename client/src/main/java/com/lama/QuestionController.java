package com.lama;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private RadioButton answer1;

    @FXML
    private RadioButton answer2;

    @FXML
    private RadioButton answer3;

    @FXML
    private RadioButton answer4;

    @FXML
    private Button updateBtn;

    public void setData(Question question){
        this.question = question;

        questionTxt.setText(question.getQuestion());
        questionInst.setText(question.getInstructions());
        answer1.setText(question.getAnswer1());
        answer2.setText(question.getAnswer2());
        answer3.setText(question.getAnswer3());
        answer4.setText(question.getAnswer4());

        ToggleGroup toggleGroup = new ToggleGroup();

        answer1.setToggleGroup(toggleGroup);
        answer2.setToggleGroup(toggleGroup);
        answer3.setToggleGroup(toggleGroup);
        answer4.setToggleGroup(toggleGroup);
    }

    public void isTest(){
        this.updateBtn.setVisible(false);
    }

    public int chosenAnswer(){
        if(answer1.isSelected())
            return 1;
        if(answer2.isSelected())
            return 2;
        if(answer3.isSelected())
            return 3;
        if(answer4.isSelected())
            return 4;
        return 5;
    }

    public void setCorrectAnswer(int correct){
        switch (correct){
            case 1:
                answer1.setStyle("-fx-text-fill: green;");
                break;
            case 2:
                answer2.setStyle("-fx-text-fill: green;");
                break;
            case 3:
                answer3.setStyle("-fx-text-fill: green;");
                break;
            default:
                answer4.setStyle("-fx-text-fill: green;");
                break;
        }
    }

    public void setWrongAnswer(int wrong){
        switch (wrong){
            case 1:
                answer1.setStyle("-fx-text-fill: red;");
                break;
            case 2:
                answer2.setStyle("-fx-text-fill: red;");
                break;
            case 3:
                answer3.setStyle("-fx-text-fill: red;");
                break;
            default:
                answer4.setStyle("-fx-text-fill: red;");
                break;
        }
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

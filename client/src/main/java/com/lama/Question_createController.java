package com.lama;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Question_createController {

    @FXML
    private TextField questionTxt;

    @FXML
    private TextField instTxt;

    @FXML
    private TextField ans1Txt;

    @FXML
    private TextField ans2Txt;

    @FXML
    private TextField ans3Txt;

    @FXML
    private TextField ans4Txt;

    @FXML
    private TextField correctChoice;

    @FXML
    private Button saveBtn;

    private String question_number;

    public void setData(String question, String instructions, String ans1, String ans2, String ans3, String ans4, int correctAns, String question_number){
        questionTxt.setText(question);
        instTxt.setText(instructions);
        ans1Txt.setText(ans1);
        ans2Txt.setText(ans2);
        ans3Txt.setText(ans3);
        ans4Txt.setText(ans4);
        correctChoice.setText(Integer.toString(correctAns));
        this.question_number = question_number;
    }

    public void setData(){
        questionTxt.setText("");
        instTxt.setText("");
        ans1Txt.setText("");
        ans2Txt.setText("");
        ans3Txt.setText("");
        ans4Txt.setText("");
        correctChoice.setText(Integer.toString(1));
    }

    @FXML
    void saveChanges(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) saveBtn.getScene().getWindow();
        // Close the current window
        currentStage.close();

        SimpleClient.getClient().sendToServer(new Message(111, "update_question:" + question_number
        + "#" + questionTxt.getText() + "#" + instTxt.getText() + "#" + ans1Txt.getText() + "#" + ans2Txt.getText() + "#" + ans3Txt.getText()
                + "#" + ans4Txt.getText() + "#" + correctChoice.getText()));
    }

}

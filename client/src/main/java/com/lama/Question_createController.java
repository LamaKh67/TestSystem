package com.lama;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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

    @FXML
    private ChoiceBox<String> coursesChoice;

    @FXML
    private Label courseslbl;

    @FXML
    private Label qNumlbl;

    @FXML
    private TextField qNum2;

    @FXML
    private TextField qNum1;

    @FXML
    private Button addBtn;

    private List<Course> courses;

    String chosenCourses = "#";

    private boolean isAdding = false;

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

    public void setData(List<Course> courses){
        questionTxt.setText("");
        instTxt.setText("");
        ans1Txt.setText("");
        ans2Txt.setText("");
        ans3Txt.setText("");
        ans4Txt.setText("");
        correctChoice.setText(Integer.toString(1));
        this.courses = courses;
        coursesChoice.setVisible(true);
        courseslbl.setVisible(true);
        qNum1.setVisible(true);
        qNum2.setVisible(true);
        qNumlbl.setVisible(true);
        addBtn.setVisible(true);
        boolean first = true;
        for(Course course:courses){
            if (first) {
                String subject = Integer.toString(course.getCourse_subject().getId());
                if (subject.length() < 2)
                    subject = "0" + subject;
                qNum1.setText(subject);
                first = false;
            }
            coursesChoice.getItems().add(course.getName() + "(ID: " +Integer.toString(course.getId()) + ")");
        }
        isAdding = true;
    }

    @FXML
    void addCourseOP(ActionEvent event) {
        int ind = 0;
        String course = coursesChoice.getValue();
        while(course.charAt(ind) != '(')
            ind++;
        ind += 5;
        String course_id = course.substring(ind, course.length()-1);
        chosenCourses += course_id + "#";
    }

    @FXML
    void saveChanges(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) saveBtn.getScene().getWindow();
        // Close the current window
        currentStage.close();

        if(!isAdding) {
            SimpleClient.getClient().sendToServer(new Message(111, "update_question:" + question_number
                    + "#" + questionTxt.getText() + "#" + instTxt.getText() + "#" + ans1Txt.getText() + "#" + ans2Txt.getText() + "#" + ans3Txt.getText()
                    + "#" + ans4Txt.getText() + "#" + correctChoice.getText()));
        }else{
            question_number = qNum1.getText() + qNum2.getText();
            SimpleClient.getClient().sendToServer(new Message(112, "add_question:#" + question_number
                    + "#" + questionTxt.getText() + "#" + instTxt.getText() + "#" + ans1Txt.getText() + "#" + ans2Txt.getText() + "#" + ans3Txt.getText()
                    + "#" + ans4Txt.getText() + "#" + correctChoice.getText() + chosenCourses));
        }
    }
}
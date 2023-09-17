package com.lama;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
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
    private TextField qNum2;

    @FXML
    private TextField qNum1;

    @FXML
    private Button addBtn;

    @FXML
    private Label errorLbl;

    @FXML
    private Label subjectlbl;

    @FXML
    private ChoiceBox<String> subjectChoice;

    @FXML
    private Label addedCrs;

    private List<Course> courses;

    String chosenCourses = "#";

    private boolean isAdding = false;

    private boolean first = true;

    private HashMap<String, Integer> hashMap = new HashMap<>();

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
        subjectChoice.setVisible(true);
        subjectlbl.setVisible(true);
        addBtn.setVisible(true);

        for(Course course:courses){
            String sub_name = course.getCourse_subject().getName();
            if(hashMap.containsKey(sub_name))
                continue;
            hashMap.put(sub_name, course.getCourse_subject().getId());
            subjectChoice.getItems().add(sub_name);
        }
        isAdding = true;
    }

    @FXML
    void addCourseOP(ActionEvent event) {
        int ind = 0;
        String course = coursesChoice.getValue();
        if(!chosenCourses.equals("#"))
            addedCrs.setText(addedCrs.getText() + ",");
        while(course.charAt(ind) != '(')
            ind++;
        ind += 5;
        String course_id = course.substring(ind, course.length()-1);
        chosenCourses += course_id + "#";
        addedCrs.setText(addedCrs.getText() + course);
    }


    @FXML
    void courseChoiceOP(MouseEvent event) {
        if (coursesChoice.getItems().size() > 0)
            coursesChoice.getItems().clear();
        if(first){
            String subject = Integer.toString(hashMap.get(subjectChoice.getValue()));
            if (subject.length() < 2)
                subject = "0" + subject;
            qNum1.setText(subject);
            first = false;
        }
        for(int i = 0; i < courses.size(); i++){
            String sub_name = courses.get(i).getCourse_subject().getName();
            if(sub_name.equals(subjectChoice.getValue().toString())) {
                coursesChoice.getItems().add(courses.get(i).getName() +
                        "(ID: " +Integer.toString(courses.get(i).getId()) + ")");
            }
        }
        coursesChoice.hide();
        coursesChoice.show();
    }

    @FXML
    void subChoiceOP(MouseEvent event) {
        addedCrs.setText("");
        chosenCourses = "#";
        first = true;
    }

    @FXML
    void saveChanges(ActionEvent event) throws IOException {
        if(questionTxt.getText().equals("") || instTxt.getText().equals("") || ans1Txt.getText().equals("") ||
                ans2Txt.getText().equals("") || ans3Txt.getText().equals("") || ans4Txt.getText().equals("") ||
                correctChoice.getText().equals("")){
            errorLbl.setVisible(true);
            errorLbl.setText("All fields are mandatory!");
            return;
        }
        else if(isAdding && chosenCourses.equals("#")){
            errorLbl.setVisible(true);
            errorLbl.setText("Choose at least one course!");
            return;
        }
        Stage currentStage = (Stage) saveBtn.getScene().getWindow();
        currentStage.close();

        if(!isAdding) {
            SimpleClient.getClient().sendToServer(new Message(111, "update_question:" + question_number
                    + "#" + questionTxt.getText() + "#" + instTxt.getText() + "#" + ans1Txt.getText() + "#" + ans2Txt.getText() + "#" + ans3Txt.getText()
                    + "#" + ans4Txt.getText() + "#" + correctChoice.getText()));
            return;
        }

        int max = 1;
        Subject subject = null;
        for(Course course:courses){
            if(course.getCourse_subject().getName().equals(subjectChoice.getValue())){
                subject = course.getCourse_subject();
                break;
            }
        }

        for(Course course:subject.getCourses()){
            for(Question question:course.getQuestions()){
                if(question.getId() > max)
                    max = question.getId();
            }
        }

        max++;
        if(max > 99){
            qNum2.setText(Integer.toString(max));
        }else if(max > 9){
            qNum2.setText("0" + Integer.toString(max));
        }else {
            qNum2.setText("00" + Integer.toString(max));
        }

        question_number = qNum1.getText() + qNum2.getText();
        SimpleClient.getClient().sendToServer(new Message(112, "add_question:#" + question_number
                + "#" + questionTxt.getText() + "#" + instTxt.getText() + "#" + ans1Txt.getText() + "#" + ans2Txt.getText() + "#" + ans3Txt.getText()
                + "#" + ans4Txt.getText() + "#" + correctChoice.getText() + chosenCourses));
    }
}
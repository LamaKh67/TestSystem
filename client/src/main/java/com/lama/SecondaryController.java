package com.lama;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
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

    @FXML
    private Button showBtn;

    private Teacher teacher;

    int message_id = 10;

    public void setData(Teacher teacher){
        this.teacher = teacher;
    }

//    @FXML
//    void initialize() {
//    }

    @FXML
    void addQuestion(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("question_create.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Question_createController itemController = fxmlLoader.getController();
        itemController.setData(teacher.getCourses());
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

    @Subscribe
    public void onAddQuestion(Message message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("question_create.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Question_createController itemController = fxmlLoader.getController();
        itemController.setData(teacher.getCourses());
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

    @FXML
    void showQuestions(ActionEvent event) throws IOException {
        for(int i = 0; i < teacher.getCourses().size(); i++) {
            Message message = new Message(6,"");
            List<Question> questionList = teacher.getCourses().get(i).getQuestions();
            StringBuffer questions = new StringBuffer();

            for (Question question : questionList) {
                questions.append(question.getQuestion_number());
                questions.append('#');
            }
            message.setMessage(questions.toString());
            onShowQuestionsNumbers(message);
        }
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
            buttons[i].setText("Question " + question_numbers[i]);
            buttons[i].setStyle("-fx-background-color: #BAEAC3; " +
                    "-fx-background-radius: 30; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-style: solid; " +
                    "-fx-border-color: black; " +
                    "-fx-font-weight: bold;");
            buttons[i].setMinWidth(250);
            questionNumbers.add(buttons[i], 0, row);

            int finalI = i;
            buttons[i].setOnAction(e -> {
                try {
                    if(!EventBus.getDefault().isRegistered(this))
                        EventBus.getDefault().register(this);
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

    @FXML
    void signOutOP(ActionEvent event) throws IOException {
        SimpleClient.getClient().sendToServer(new Message(210, "signOut#" + Integer.toString(teacher.getId())));
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("primary.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // Setting the title and the icon behind the title.
        URL url = getClass().getResource("/images/icon.png");
        String path = url.toExternalForm();
        Image icon = new Image(path);
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        Label titleLabel = new Label("Title");
        titleLabel.setGraphic(imageView);
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        SimpleChatClient.stage.setScene(scene);
        SimpleChatClient.stage.show();
    }
}

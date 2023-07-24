package com.lama;

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

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class StudentController {

    @FXML
    private Button showBtn;

    @FXML
    private GridPane coursesGrid;

    private Student student;

    public void setData(Student student){
        this.student = student;
    }

    @FXML
    void showCoursesOP(ActionEvent event) {
        List<Course> courses = student.getCourses();

        Button[] buttons = new Button[courses.size()];
        int row=0;
        for(int i = 0; i < courses.size(); i++){
            row++;
            buttons[i] = new Button();
            String name = "Course " + courses.get(i).getName() + Integer.toString(courses.get(i).getId());
            buttons[i].setText(name);
            buttons[i].setStyle("-fx-background-color: #BAEAC3; " +
                    "-fx-background-radius: 30; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-style: solid; " +
                    "-fx-border-color: black; " +
                    "-fx-font-weight: bold;");
            buttons[i].setMinWidth(250);
            coursesGrid.add(buttons[i], 0, row);

            int finalI = i;
            buttons[i].setOnAction(e -> {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("test.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                TestController itemController = fxmlLoader.getController();
                itemController.setData(courses.get(finalI));
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
            });

        }
    }

    @FXML
    void signOutOP(ActionEvent event) throws IOException {
        SimpleClient.getClient().sendToServer(new Message(208, "signOut#" + Integer.toString(student.getId())));
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

package com.lama;


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.collections.FXCollections;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ManagerController {

    @FXML
    private ChoiceBox<String> subjectsChoice;

    @FXML
    private BarChart<String, Number> histogram;

    private Manager manager;

    private Message final_message = new Message(202, "");

    public void setData(Manager manager) {
        this.manager = manager;
        for(Subject subject:manager.subjects){
            System.out.println(subject.getId());
            subjectsChoice.getItems().add(subject.getName() + "(ID: " +Integer.toString(subject.getId()) + ")");
        }
    }

    @FXML
    void subHistogramOP(ActionEvent event) throws IOException {
        int ind = 0;
        if(subjectsChoice.getValue() == null)
            return;
        String subject = subjectsChoice.getValue();
        while(subject.charAt(ind) != '(')
            ind++;
        ind += 5;
        String subject_id = subject.substring(ind, subject.length()-1);
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        final_message = new Message(201, "getSubjectHistogram#" + subject_id);
        SimpleClient.getClient().sendToServer(final_message);
    }

    @Subscribe
    public void showSubjectHistogram(Message message){
        Platform.runLater(() -> {
            if(message.getMessage().equals("refresh")){
                if(!final_message.getMessage().equals("")) {
                    try {
                        SimpleClient.getClient().sendToServer(final_message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return;
            }
            histogram.getData().clear();
            CategoryAxis temp = (CategoryAxis) histogram.getXAxis();
            temp.getCategories().clear();

            HashMap<String, Integer> dataMap = message.hist;

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
                if(entry.getValue() == 0)
                    continue;
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            histogram.getData().add(series);
            CategoryAxis xAxis = (CategoryAxis) histogram.getXAxis();
            xAxis.setCategories(FXCollections.observableArrayList(dataMap.keySet()));
            xAxis.setLabel("Courses");
            xAxis.setTickLabelRotation(90);

            NumberAxis yAxis = (NumberAxis) histogram.getYAxis();
            yAxis.setLabel("Number Of Questions");
            yAxis.setTickLabelRotation(90);
        });
    }

    @FXML
    void signOutOP(ActionEvent event) throws IOException {
        SimpleClient.getClient().sendToServer(new Message(209, "signOut#" + Integer.toString(manager.getId())));
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

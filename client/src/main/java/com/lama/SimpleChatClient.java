package com.lama;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    public static Stage stage;
    private SimpleClient client;

    @Override
    public void start(Stage stage) throws IOException {
//    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
        SimpleChatClient.stage = stage;
    	client.openConnection();
        scene = new Scene(loadFXML("primary"));
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
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        SimpleChatClient.stage.setScene(scene);
        SimpleChatClient.stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
		super.stop();
	}


    @Subscribe
    public void onMessageEvent(ErrorEvent message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION,
                    String.format("Message:\nId: %d\nData: %s\nTimestamp: %s\n"
            ));
            alert.setTitle("new message");
            alert.setHeaderText("New Message:");
            alert.show();
        });
    }


	public static void main(String[] args) {
        launch();
    }

}
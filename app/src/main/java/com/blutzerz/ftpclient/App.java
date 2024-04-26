package com.blutzerz.ftpclient;

import com.blutzerz.ftpclient.controllers.DashboardController;

// import com.blutzerz.ftpclient.controllers.DashboardController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));

        Parent root = loader.load();
        DashboardController controller = loader.getController();

        controller.initialize();

        primaryStage.setTitle("FTP Dashboard");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

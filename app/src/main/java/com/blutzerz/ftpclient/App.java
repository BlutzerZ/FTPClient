package com.blutzerz.ftpclient;

import com.blutzerz.ftpclient.controllers.DashboardController;
import com.blutzerz.ftpclient.controllers.LoginController;
import com.blutzerz.ftpclient.engine.FTPEngine;

// import com.blutzerz.ftpclient.controllers.DashboardController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        showLogin();
    }

    public void showLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        LoginController loginController = loader.getController();
        loginController.setApp(this);
        stage.setScene(scene);
        stage.show();
    }

    public void showDashboard(FTPEngine ftpEngine) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/dashboard.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.initialize(ftpEngine, this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
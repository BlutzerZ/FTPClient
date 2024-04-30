package com.blutzerz.ftpclient.controllers;

import com.blutzerz.ftpclient.App;
import com.blutzerz.ftpclient.engine.FTPEngine;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML
    private TextField GUILoginHost;

    @FXML
    private TextField GUILoginPort;

    @FXML
    private TextField GUILoginUsername;

    @FXML
    private PasswordField GUILoginPassword;

    @FXML
    private Button GUIConnectButton;

    @FXML
    private Label GUIFailedLoginLabel;

    private FTPEngine ftpEngine;

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    public void initialize() {
        GUIFailedLoginLabel.setVisible(false);
        GUIConnectButton.setOnMouseClicked((event) -> {
            GUIConnectButton.setText("Connecting...");
            this.connect();
        });
    }

    @FXML
    public void connect() {
        String host = GUILoginHost.getText();
        int port = Integer.parseInt(GUILoginPort.getText());
        String username = GUILoginUsername.getText();
        String password = GUILoginPassword.getText();
        ftpEngine = new FTPEngine(host, port, username, password);
        if (ftpEngine != null && ftpEngine.isConnected()) {
            try {
                app.showDashboard(ftpEngine);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            GUIConnectButton.setText("Connect");
            GUIFailedLoginLabel.setVisible(true);
        }
    }

    @FXML
    public void disconnect() {
        if (ftpEngine != null) {
            ftpEngine.close();
        }
    }
}

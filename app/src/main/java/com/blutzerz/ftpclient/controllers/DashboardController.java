// package com.blutzerz.ftpclient.controllers;

// import javafx.fxml.FXML;
// import javafx.scene.control.TreeItem;
// import javafx.scene.control.TreeView;
// import java.io.File;

// public class DashboardController {
// @FXML
// private TreeView<String> remoteTreeView;

// @FXML
// private TreeView<String> localTreeView;

// public void initialize() {
// // Memuat data remote server
// TreeItem<String> remoteRoot = new TreeItem<>("Remote");
// remoteRoot.setExpanded(true);
// loadTreeView(new File("/home/blutzerz/CODE"), remoteRoot);
// remoteTreeView.setRoot(remoteRoot);

// // Memuat data folder lokal
// TreeItem<String> localRoot = new TreeItem<>("Local");
// localRoot.setExpanded(true);
// loadTreeView(new File("/home/blutzerz/Documents"), localRoot);
// localTreeView.setRoot(localRoot);
// }
// }

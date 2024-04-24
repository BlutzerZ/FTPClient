// package com.blutzerz.ftpclient.controllers;

// import javafx.fxml.FXML;

// public class LoginController {
// @FXML
// public void initialize() {
// // Memuat data remote server
// TreeItem<String> remoteRoot = new TreeItem<>("Remote");
// remoteRoot.setExpanded(true);
// loadTreeView(new File("/path/to/remote/directory"), remoteRoot);
// remoteTreeView.setRoot(remoteRoot);

// // Memuat data folder lokal
// TreeItem<String> localRoot = new TreeItem<>("Local");
// localRoot.setExpanded(true);
// loadTreeView(new File("/path/to/local/directory"), localRoot);
// localTreeView.setRoot(localRoot);
// }

// // Metode rekursif untuk memuat data ke dalam TreeView
// private void loadTreeView(File directory, TreeItem<String> parentItem) {
// if (directory != null && directory.exists() && directory.isDirectory()) {
// for (File file : directory.listFiles()) {
// TreeItem<String> item = new TreeItem<>(file.getName());
// if (file.isDirectory()) {
// loadTreeView(file, item);
// }
// parentItem.getChildren().add(item);
// }
// }
// }
// }

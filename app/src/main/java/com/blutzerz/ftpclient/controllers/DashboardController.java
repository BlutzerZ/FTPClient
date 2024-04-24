package com.blutzerz.ftpclient.controllers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import com.blutzerz.ftpclient.engine.FTPEngine;
import com.blutzerz.ftpclient.engine.LocalStorageEngine;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class DashboardController {

    @FXML
    private TextField GUILocalDir;

    @FXML
    private TextField GUIRemoteDir;

    @FXML
    private TreeView<FileInfo> GUILocalTreeView;

    @FXML
    private TreeView<FileInfo> GUIRemoteTreeView;

    @FXML
    private TreeTableView<String> GUILocalTreeTableView;

    @FXML
    private TreeTableView<String> GUIRemoteTreeTableView;

    public void initialize() throws IOException {
        TreeItem<FileInfo> localRoot = new TreeItem<>(new FileInfo("Local", ""));
        localRoot.setExpanded(true);
        LocalStorageEngine localDir = new LocalStorageEngine();
        loadLocalTreeView(new File(localDir.getStringCurrentDirectory()), localRoot);
        GUILocalTreeView.setRoot(localRoot);

        TreeItem<FileInfo> remoteRoot = new TreeItem<>(new FileInfo("Remote", ""));
        remoteRoot.setExpanded(true);
        System.out.println("YOHOHOHOH");
        FTPEngine remoteDir = new FTPEngine(
                "files.000webhost.com",
                21,
                "blutzerz-ftp-client-test",
                "tNb2dRx![2Qw7LUf24");
        loadRemoteTreeView(remoteDir, "/", remoteRoot);
        GUIRemoteTreeView.setRoot(remoteRoot);
        // Set

        // Menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem createMenuItem = new MenuItem("Create Folder");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(openMenuItem, createMenuItem, deleteMenuItem);
        GUILocalTreeView.setOnContextMenuRequested(event -> {
            TreeItem<FileInfo> selectedItem = GUILocalTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                contextMenu.show(GUILocalTreeView, event.getScreenX(), event.getScreenY());
            }
        });
        GUILocalTreeView.setOnMouseClicked((event) -> handleMouseClick(event, GUILocalTreeView));

        // Event
        openMenuItem.setOnAction((event) -> openMenuItemHandler(event, GUILocalTreeView));
        createMenuItem.setOnAction((event) -> createMenuItemHandler(event, GUILocalTreeView));
        deleteMenuItem.setOnAction((event) -> deleteMenuItemHandler(event, GUILocalTreeView));

    }

    private void loadLocalTreeView(File directory, TreeItem<FileInfo> parentItem) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    FileInfo fileInfo = new FileInfo(file.getName(), file.getAbsolutePath());
                    TreeItem<FileInfo> item = new TreeItem<>(fileInfo);

                    loadLocalTreeView(file, item);
                    parentItem.getChildren().add(item);
                }
            }
        }
    }

    private void loadRemoteTreeView(FTPEngine remoteDir, String parentDir, TreeItem<FileInfo> parentItem)
            throws IOException {
        remoteDir.changeDirectory(parentDir);
        for (org.apache.commons.net.ftp.FTPFile ftpFile : remoteDir.getFilesDirectory()) {
            if (ftpFile.isDirectory() || !ftpFile.getName().equals(".")) {
                FileInfo fileInfo = new FileInfo(ftpFile.getName(), parentDir + ftpFile.getName() + "/");
                TreeItem<FileInfo> item = new TreeItem<>(fileInfo);
                parentItem.getChildren().add(item);
                loadRemoteTreeView(remoteDir, parentDir + ftpFile.getName() + "/", item);
            }
        }
    }

    // Event -----------------------------------------------

    private void openMenuItemHandler(ActionEvent t, TreeView<FileInfo> tree) {

    }

    private void createMenuItemHandler(ActionEvent t, TreeView<FileInfo> tree) {
        TreeItem newFolder = new TreeItem<FileInfo>(new FileInfo("FolderName", "xxx"));
        tree.getSelectionModel().getSelectedItem().getChildren().add(newFolder);
    }

    private void deleteMenuItemHandler(ActionEvent t, TreeView<FileInfo> tree) {

    }

    private void handleMouseClick(MouseEvent event, TreeView<FileInfo> tree) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            FileInfo fileInfo = GUIRemoteTreeView.getSelectionModel().getSelectedItem().getValue();
            if (fileInfo != null) {
                System.out.println("Double click on: " + fileInfo.getName());
                System.out.println("Path: " + fileInfo.getPath());
            }
        }
    }

}

package com.blutzerz.ftpclient.controllers;

import java.io.File;
import java.io.IOException;

import com.blutzerz.ftpclient.engine.FTPEngine;
import com.blutzerz.ftpclient.engine.LocalStorageEngine;

import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class DashboardController {

    @FXML
    protected TextField GUILocalDir;

    @FXML
    protected TextField GUIRemoteDir;

    @FXML
    protected TreeView<FileInfo> GUILocalTreeView;

    @FXML
    protected TreeView<FileInfo> GUIRemoteTreeView;

    @FXML
    protected TreeTableView<String> GUILocalTreeTableView;

    @FXML
    protected TreeTableView<String> GUIRemoteTreeTableView;

    LocalStorageEngine localDir;

    FTPEngine remoteDir;

    public void initialize() throws IOException {
        TreeItem<FileInfo> localRoot = new TreeItem<>(new FileInfo("Local", ""));
        localRoot.setExpanded(true);
        localDir = new LocalStorageEngine();
        System.out.println(localDir.getStringCurrentDirectory());
        loadLocalTreeView(localDir.getCurrentDirectory(), localRoot);
        GUILocalTreeView.setRoot(localRoot);
        GUILocalDir.setText(localDir.getStringCurrentDirectory());

        TreeItem<FileInfo> remoteRoot = new TreeItem<>(new FileInfo("Remote", ""));
        remoteRoot.setExpanded(true);
        System.out.println("YOHOHOHOH");
        remoteDir = new FTPEngine(
                "files.000webhost.com",
                21,
                "blutzerz-ftp-client-test",
                "tNb2dRx![2Qw7LUf24");
        loadRemoteTreeView("/", remoteRoot);
        GUIRemoteTreeView.setRoot(remoteRoot);
        GUIRemoteDir.setText(remoteDir.getStringDirectory());

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
        GUIRemoteTreeView.setOnContextMenuRequested(event -> {
            TreeItem<FileInfo> selectedItem = GUIRemoteTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                contextMenu.show(GUIRemoteTreeView, event.getScreenX(), event.getScreenY());
            }
        });

        // Event
        GUILocalTreeView.setOnMouseClicked((event) -> {
            handleMouseClick(event, GUILocalTreeView, "local");
        });
        GUIRemoteTreeView.setOnMouseClicked((event) -> {
            handleMouseClick(event, GUIRemoteTreeView, "remote");
        });

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

                    if (parentItem.getChildren().isEmpty()) {
                        parentItem.getChildren().add(item);
                    } else {
                        for (TreeItem<FileInfo> iterable_element : parentItem.getChildren()) {
                            if (!iterable_element.getValue().getName().equals(item.getValue().getName())) {
                                System.out.println("Beda nih: " + iterable_element.getValue().getName()
                                        + item.getValue().getName());
                                parentItem.getChildren().add(item);
                                break;
                            } else {
                                System.out.println("SHEET FAIL");
                                System.out.println(iterable_element.getValue().getName() +
                                        item.getValue().getName());
                            }
                        }
                    }
                }
            }
        }
    }

    private void loadRemoteTreeView(String parentDir, TreeItem<FileInfo> parentItem)
            throws IOException {
        remoteDir.changeDirectory(parentDir);
        for (org.apache.commons.net.ftp.FTPFile ftpFile : remoteDir.getFilesDirectory()) {
            if (ftpFile.isDirectory()) {
                FileInfo fileInfo = new FileInfo(ftpFile.getName(), parentDir + ftpFile.getName() + "/");
                TreeItem<FileInfo> item = new TreeItem<>(fileInfo);

                if (parentItem.getChildren().isEmpty()) {
                    parentItem.getChildren().add(item);
                } else {
                    for (TreeItem<FileInfo> iterable_element : parentItem.getChildren()) {
                        if (!iterable_element.getValue().getName().equals(item.getValue().getName())) {
                            // System.out.println("Beda nih: " + iterable_element.getValue().getName()
                            // + item.getValue().getName());
                            parentItem.getChildren().add(item);
                            break;
                        } else {
                            // System.out.println("SHEET FAIL");
                            // System.out.println(iterable_element.getValue().getName() +
                            // item.getValue().getName());
                            continue;
                        }
                    }
                }
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

    private void handleMouseClick(MouseEvent event, TreeView<FileInfo> tree, String type) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            FileInfo fileInfo = tree.getSelectionModel().getSelectedItem().getValue();
            if (fileInfo != null) {
                System.out.println("Double click on: " + fileInfo.getName());
                System.out.println("Path: " + fileInfo.getPath());

                if (type == "local") {
                    localDir.changeDirecotry(fileInfo.getPath());
                    loadLocalTreeView(localDir.getCurrentDirectory(), tree.getSelectionModel().getSelectedItem());
                    GUILocalDir.setText(localDir.getStringCurrentDirectory());
                } else if (type == "remote") {
                    try {
                        remoteDir.changeDirectory(fileInfo.getPath());
                        loadRemoteTreeView(remoteDir.getStringDirectory(), tree.getSelectionModel().getSelectedItem());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GUIRemoteDir.setText(remoteDir.getStringDirectory());

                    for (org.apache.commons.net.ftp.FTPFile ftpFile : remoteDir.getFilesDirectory()) {
                        if (ftpFile.isFile()) {
                            FileInfo finfo = new FileInfo(ftpFile.getName(),
                                    tree.getSelectionModel().getSelectedItem() + ftpFile.getName());
                            TreeItem<FileInfo> item = new TreeItem<>(finfo);

                            System.out.println(item);
                        }
                    }

                }
            }
        }
    }

    public void openFolder(String type) {
        if (type == "local") {

        } else if (type == "remote") {

        } else {
            System.out.println("Invalid Argument");
        }
    }
}

package com.blutzerz.ftpclient.controllers;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;

import org.apache.commons.net.ftp.FTPFile;

import com.blutzerz.ftpclient.engine.FTPEngine;
import com.blutzerz.ftpclient.engine.LocalStorageEngine;

import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<DirInfo> GUILocalTableView;

    @FXML
    private TableColumn<DirInfo, String> GUILocalTableColumnFilename;

    @FXML
    private TableColumn<DirInfo, String> GUILocalTableColumnType;

    @FXML
    private TableColumn<DirInfo, Integer> GUILocalTableColumnSize;

    @FXML
    private TableColumn<DirInfo, String> GUILocalTableColumnLastMod;

    @FXML
    private TableView<DirInfo> GUIRemoteTableView;

    @FXML
    private TableColumn<DirInfo, String> GUIRemoteTableColumnFilename;

    @FXML
    private TableColumn<DirInfo, String> GUIRemoteTableColumnType;

    @FXML
    private TableColumn<DirInfo, String> GUIRemoteTableColumnSize;

    @FXML
    private TableColumn<DirInfo, String> GUIRemoteTableColumnLastMod;

    private LocalStorageEngine localDir;

    private FTPEngine remoteDir;

    public void initialize() throws IOException {
        TreeItem<FileInfo> localRoot = new TreeItem<>(new FileInfo("Local", ""));
        localRoot.setExpanded(true);
        localDir = new LocalStorageEngine();
        System.out.println(localDir.getStringCurrentDirectory());
        loadLocalTreeView(localDir.getCurrentDirectory(), localRoot);
        GUILocalTreeView.setRoot(localRoot);
        GUILocalDir.setText(localDir.getStringCurrentDirectory());
        loadLocalTableView(localDir.getCurrentDirectory(), localRoot);

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
        loadRemoteTableView(localDir.getStringCurrentDirectory(), remoteRoot);

        GUILocalTableColumnFilename.setCellValueFactory(new PropertyValueFactory<>("name"));
        GUILocalTableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        GUILocalTableColumnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        GUILocalTableColumnLastMod.setCellValueFactory(new PropertyValueFactory<>("lastMod"));

        GUIRemoteTableColumnFilename.setCellValueFactory(new PropertyValueFactory<>("name"));
        GUIRemoteTableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        GUIRemoteTableColumnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        GUIRemoteTableColumnLastMod.setCellValueFactory(new PropertyValueFactory<>("lastMod"));

        // Menu
        ContextMenu treeViewMenu = new ContextMenu();
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem createMenuItem = new MenuItem("Create Folder");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        treeViewMenu.getItems().addAll(openMenuItem, createMenuItem, deleteMenuItem);
        GUILocalTreeView.setOnContextMenuRequested(event -> {
            TreeItem<FileInfo> selectedItem = GUILocalTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                treeViewMenu.show(GUILocalTreeView, event.getScreenX(), event.getScreenY());
            }
        });
        GUIRemoteTreeView.setOnContextMenuRequested(event -> {
            TreeItem<FileInfo> selectedItem = GUIRemoteTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                treeViewMenu.show(GUIRemoteTreeView, event.getScreenX(), event.getScreenY());
            }
        });

        ContextMenu localTableViewMenu = new ContextMenu();
        MenuItem localTableViewUploadMenuItem = new MenuItem("Upload");
        MenuItem localTableViewdeleteMenuItem = new MenuItem("Delete");
        localTableViewMenu.getItems().addAll(localTableViewUploadMenuItem, localTableViewdeleteMenuItem);
        GUILocalTableView.setOnContextMenuRequested(event -> {
            DirInfo selectedItem = GUILocalTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                localTableViewMenu.show(GUILocalTableView, event.getScreenX(), event.getScreenY());
            }
        });

        ContextMenu remoteTableViewMenu = new ContextMenu();
        MenuItem remoteTableViewDownloadMenuItem = new MenuItem("Download");
        MenuItem remoteTableViewDeleteMenuItem = new MenuItem("Delete");
        remoteTableViewMenu.getItems().addAll(remoteTableViewDownloadMenuItem, remoteTableViewDeleteMenuItem);
        GUIRemoteTableView.setOnContextMenuRequested(event -> {
            DirInfo selectedItem = GUIRemoteTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                remoteTableViewMenu.show(GUIRemoteTableView, event.getScreenX(), event.getScreenY());
            }
        });

        // Event
        GUILocalTreeView.setOnMouseClicked((event) -> {
            doubleClickTree(event, GUILocalTreeView, "local");
        });
        GUIRemoteTreeView.setOnMouseClicked((event) -> {
            doubleClickTree(event, GUIRemoteTreeView, "remote");
        });

        openMenuItem.setOnAction((event) -> openMenuItemHandler(event, GUILocalTreeView));
        createMenuItem.setOnAction((event) -> createMenuItemHandler(event, GUILocalTreeView));
        deleteMenuItem.setOnAction((event) -> deleteMenuItemHandler(event, GUILocalTreeView));

        localTableViewUploadMenuItem.setOnAction((event) -> {
            for (int i = 0; i < 5; i++) {
                try {
                    remoteDir.uploadFile(localDir.getStringCurrentDirectory() + "/"
                            + GUILocalTableView.getSelectionModel().getSelectedItem().getName());
                    break;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    remoteDir.close();
                    remoteDir.open();
                }
            }
            TreeItem<FileInfo> selectedRemoteDir = new TreeItem<>(new FileInfo(
                    GUILocalTableView.getSelectionModel().getSelectedItem().getName(),
                    GUILocalTableView.getSelectionModel().getSelectedItem().getPath()));
            loadRemoteTableView(remoteDir.getStringDirectory(),
                    selectedRemoteDir);
        });

        remoteTableViewDownloadMenuItem.setOnAction((event) -> {
            remoteDir.downloadFile(GUIRemoteTableView.getSelectionModel().getSelectedItem().getName(),
                    localDir.getStringCurrentDirectory());

            TreeItem<FileInfo> selectedLocalDir = new TreeItem<>(new FileInfo(
                    GUIRemoteTableView.getSelectionModel().getSelectedItem().getName(),
                    GUIRemoteTableView.getSelectionModel().getSelectedItem().getPath()));
            loadLocalTableView(localDir.getCurrentDirectory(), selectedLocalDir);
        });

        GUILocalTableView.setOnMouseClicked((event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                System.out.println("MUEHEHE");
                TreeItem<FileInfo> selectedLocalDir = new TreeItem<>(new FileInfo(
                        GUILocalTableView.getSelectionModel().getSelectedItem().getName(),
                        GUILocalTableView.getSelectionModel().getSelectedItem().getPath()));
                localDir.changeDirecotry(selectedLocalDir.getValue().getPath());
                loadLocalTableView(localDir.getCurrentDirectory(), selectedLocalDir);
            }
        });

        GUIRemoteTableView.setOnMouseClicked((event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                System.out.println("MUEHEHE");
                TreeItem<FileInfo> selectedRemoteDir = new TreeItem<>(new FileInfo(
                        GUIRemoteTableView.getSelectionModel().getSelectedItem().getName(),
                        GUIRemoteTableView.getSelectionModel().getSelectedItem().getPath()));
                loadRemoteTableView(remoteDir.getStringDirectory(),
                        selectedRemoteDir);
            }
        });
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
                        boolean found = false;
                        for (TreeItem<FileInfo> iterable_element : parentItem.getChildren()) {
                            if (iterable_element.getValue().getName().equals(item.getValue().getName())) {
                                found = true;
                            } else {
                                continue;
                            }
                        }
                        if (!found) {
                            parentItem.getChildren().add(item);
                        }
                    }
                }
            }
        }
    }

    private void loadRemoteTreeView(String parentDir, TreeItem<FileInfo> parentItem) {
        remoteDir.changeDirectory(parentDir);
        parentItem.getChildren().clear();
        for (int i = 0; i < 5; i++) {
            if (remoteDir.getFilesDirectory() == null) {
                remoteDir.open();
            }
        }

        for (FTPFile ftpFile : remoteDir.getFilesDirectory()) {
            if (ftpFile.isDirectory() && !(ftpFile.getName().equals(".") || ftpFile.getName().equals(".."))) {

                FileInfo fileInfo = new FileInfo(ftpFile.getName(), parentDir + ftpFile.getName() + "/");
                TreeItem<FileInfo> item = new TreeItem<>(fileInfo);

                parentItem.getChildren().add(item);
            }
        }
    }

    private void loadLocalTableView(File directory, TreeItem<FileInfo> parenItem) {
        GUILocalTableView.getItems().clear();
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            Arrays.sort(files);
            for (File file : files) {
                if (file.isFile()) {
                    GUILocalTableView.getItems()
                            .add(new DirInfo(file.getName(), file.getAbsolutePath(), "File",
                                    file.length(),
                                    file.lastModified()));
                } else if (file.isDirectory()) {
                    GUILocalTableView.getItems()
                            .add(new DirInfo(file.getName(), file.getAbsolutePath(), "Dir",
                                    file.length(),
                                    file.lastModified()));
                }
            }
        }
    }

    private void loadRemoteTableView(String parentDir, TreeItem<FileInfo> parentItem) {
        GUIRemoteTableView.getItems().clear();

        remoteDir.changeDirectory(parentDir);
        for (FTPFile ftpFile : remoteDir.getFilesDirectory()) {
            if (ftpFile.isFile()) {
                System.out.println(ftpFile);
                GUIRemoteTableView.getItems()
                        .add(new DirInfo(ftpFile.getName(), parentDir + ftpFile.getName() + "/", "File",
                                ftpFile.getSize(),
                                ftpFile.getTimestamp().getTime().getTime()));
            } else if (ftpFile.isDirectory() && !(ftpFile.getName().equals(".") || ftpFile.getName().equals(".."))) {
                GUIRemoteTableView.getItems()
                        .add(new DirInfo(ftpFile.getName(), parentDir + ftpFile.getName() + "/", "Dir",
                                ftpFile.getSize(),
                                ftpFile.getTimestamp().getTime().getTime()));
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

    private void doubleClickTree(MouseEvent event, TreeView<FileInfo> tree, String type) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            FileInfo fileInfo = tree.getSelectionModel().getSelectedItem().getValue();
            if (fileInfo != null) {
                System.out.println("Double click on: " + fileInfo.getName());
                System.out.println("Path: " + fileInfo.getPath());

                if (type == "local") {
                    localDir.changeDirecotry(fileInfo.getPath());
                    loadLocalTreeView(localDir.getCurrentDirectory(), tree.getSelectionModel().getSelectedItem());
                    loadLocalTableView(localDir.getCurrentDirectory(), tree.getSelectionModel().getSelectedItem());
                    GUILocalDir.setText(localDir.getStringCurrentDirectory());
                } else if (type == "remote") {

                    remoteDir.changeDirectory(fileInfo.getPath());
                    loadRemoteTreeView(remoteDir.getStringDirectory(),
                            tree.getSelectionModel().getSelectedItem());

                    loadRemoteTableView(remoteDir.getStringDirectory(),
                            tree.getSelectionModel().getSelectedItem());
                    GUIRemoteDir.setText(remoteDir.getStringDirectory());
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

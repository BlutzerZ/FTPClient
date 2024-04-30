package com.blutzerz.ftpclient.controllers;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;

import org.apache.commons.net.ftp.FTPFile;

import com.blutzerz.ftpclient.App;
import com.blutzerz.ftpclient.engine.FTPEngine;
import com.blutzerz.ftpclient.engine.LocalStorageEngine;
import com.blutzerz.ftpclient.util.DirInfo;
import com.blutzerz.ftpclient.util.FileInfo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TableColumn<DirInfo, String> GUILocalTableColumnIcon;

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
    private TableColumn<DirInfo, String> GUIRemoteTableColumnIcon;

    @FXML
    private TableColumn<DirInfo, String> GUIRemoteTableColumnFilename;

    @FXML
    private TableColumn<DirInfo, String> GUIRemoteTableColumnType;

    @FXML
    private TableColumn<DirInfo, String> GUIRemoteTableColumnSize;

    @FXML
    private TableColumn<DirInfo, String> GUIRemoteTableColumnLastMod;

    @FXML
    private MenuItem GUIMenuItemDisconnect;

    private LocalStorageEngine localDir;

    private FTPEngine remoteDir;

    private Image fileIcon;
    private Image folderIcon;

    App app;

    public void initialize(FTPEngine ftpEngine, App app) throws IOException {
        this.app = app;
        this.remoteDir = ftpEngine;
        this.localDir = new LocalStorageEngine();

        // =================================================
        // L O A D I C O N
        // =================================================

        fileIcon = new Image(DashboardController.class.getResourceAsStream("/icons/file-regular.png"), 15, 15, false,
                false);
        folderIcon = new Image(DashboardController.class.getResourceAsStream("/icons/folder-solid.png"), 15, 15, false,
                false);

        // =================================================
        // L O A D C O M P O N E N T
        // =================================================
        TreeItem<FileInfo> localRoot = new TreeItem<>(new FileInfo("Local", ""));
        localRoot.setExpanded(true);
        loadLocalTreeView(localDir.getCurrentDirectory(), localRoot);
        GUILocalTreeView.setRoot(localRoot);
        GUILocalDir.setText(localDir.getStringCurrentDirectory());
        loadLocalTableView(localDir.getCurrentDirectory(), localRoot);

        TreeItem<FileInfo> remoteRoot = new TreeItem<>(new FileInfo("Remote", ""));
        remoteRoot.setExpanded(true);

        loadRemoteTreeView("/", remoteRoot);
        GUIRemoteTreeView.setRoot(remoteRoot);
        GUIRemoteDir.setText(remoteDir.getStringDirectory());
        loadRemoteTableView(localDir.getStringCurrentDirectory(), remoteRoot);

        GUILocalTableColumnIcon.setCellValueFactory(new PropertyValueFactory<>("icon"));
        GUILocalTableColumnFilename.setCellValueFactory(new PropertyValueFactory<>("name"));
        GUILocalTableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        GUILocalTableColumnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        GUILocalTableColumnLastMod.setCellValueFactory(new PropertyValueFactory<>("lastMod"));

        GUIRemoteTableColumnIcon.setCellValueFactory(new PropertyValueFactory<>("icon"));
        GUIRemoteTableColumnFilename.setCellValueFactory(new PropertyValueFactory<>("name"));
        GUIRemoteTableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        GUIRemoteTableColumnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        GUIRemoteTableColumnLastMod.setCellValueFactory(new PropertyValueFactory<>("lastMod"));

        // =================================================
        // L O A D M E N U
        // =================================================
        ContextMenu treeViewMenu = new ContextMenu();
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem createMenuItem = new MenuItem("Create Folder");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        treeViewMenu.getItems().addAll(openMenuItem, createMenuItem, deleteMenuItem);

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

        // =================================================
        // L O A D ___ E V E N T
        // =================================================

        GUILocalTreeView.setOnMouseClicked((event) -> doubleClickTree(event, GUILocalTreeView, "local"));
        GUIRemoteTreeView.setOnMouseClicked((event) -> doubleClickTree(event, GUIRemoteTreeView, "remote"));

        openMenuItem.setOnAction((event) -> openMenuItemHandler(event, GUILocalTreeView));
        createMenuItem.setOnAction((event) -> createMenuItemHandler(event, GUILocalTreeView));
        deleteMenuItem.setOnAction((event) -> deleteMenuItemHandler(event, GUILocalTreeView));

        localTableViewUploadMenuItem.setOnAction((event) -> this.uploadFile(event));
        remoteTableViewDownloadMenuItem.setOnAction((event) -> this.downloadFile(event));

        GUILocalTableView.setOnMouseClicked((event) -> this.loadLocalDirectoryTable(event));
        GUIRemoteTableView.setOnMouseClicked((event) -> this.loadRemoteDirectoryTable(event));

        GUIMenuItemDisconnect.setOnAction((event) -> this.disconnectFTP(event));
    }

    private void loadLocalTreeView(File directory, TreeItem<FileInfo> parentItem) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    FileInfo fileInfo = new FileInfo(file.getName(), file.getAbsolutePath());
                    TreeItem<FileInfo> item = new TreeItem<>(fileInfo);

                    if (parentItem.getChildren().isEmpty()) {
                        item.setGraphic(new ImageView(folderIcon));
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
                            item.setGraphic(new ImageView(folderIcon));
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

        FTPFile[] files = null;
        int retryCount = 0;
        final int MAX_RETRIES = 5;
        while (files == null && retryCount < MAX_RETRIES) {
            try {
                files = remoteDir.getFilesDirectory();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Connection timed out. Retrying...");
                retryCount++;
                try {
                    Thread.sleep(2000); // Tunggu 2 detik sebelum mencoba kembali
                    remoteDir.open();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        for (FTPFile ftpFile : files) {
            if (ftpFile.isDirectory() && !(ftpFile.getName().equals(".") || ftpFile.getName().equals(".."))) {

                FileInfo fileInfo = new FileInfo(ftpFile.getName(), parentDir + ftpFile.getName() + "/");
                TreeItem<FileInfo> item = new TreeItem<>(fileInfo);
                item.setGraphic(new ImageView(folderIcon));
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
                            .add(new DirInfo(new ImageView(fileIcon), file.getName(), file.getAbsolutePath(), "File",
                                    file.length(),
                                    file.lastModified()));
                } else if (file.isDirectory()) {
                    GUILocalTableView.getItems()
                            .add(new DirInfo(new ImageView(folderIcon), file.getName(), file.getAbsolutePath(), "Dir",
                                    file.length(),
                                    file.lastModified()));
                }
            }
        }
    }

    private void loadRemoteTableView(String parentDir, TreeItem<FileInfo> parentItem) {
        GUIRemoteTableView.getItems().clear();

        remoteDir.changeDirectory(parentDir);
        FTPFile[] files = null;
        int retryCount = 0;
        final int MAX_RETRIES = 5;
        while (files == null && retryCount < MAX_RETRIES) {
            try {
                files = remoteDir.getFilesDirectory();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Connection timed out. Retrying...");
                retryCount++;
                try {
                    Thread.sleep(2000); // Tunggu 2 detik sebelum mencoba kembal
                    remoteDir.open();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        for (FTPFile ftpFile : files) {
            if (ftpFile.isFile()) {
                System.out.println(ftpFile);
                GUIRemoteTableView.getItems()
                        .add(new DirInfo(new ImageView(fileIcon), ftpFile.getName(),
                                parentDir + ftpFile.getName() + "/", "File",
                                ftpFile.getSize(),
                                ftpFile.getTimestamp().getTime().getTime()));
            } else if (ftpFile.isDirectory() && !(ftpFile.getName().equals(".") || ftpFile.getName().equals(".."))) {
                GUIRemoteTableView.getItems()
                        .add(new DirInfo(new ImageView(folderIcon), ftpFile.getName(),
                                parentDir + ftpFile.getName() + "/", "Dir",
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

    private void uploadFile(ActionEvent event) {
        int retryCount = 0;
        final int MAX_RETRIES = 5;
        while (retryCount < MAX_RETRIES) {
            try {
                remoteDir.uploadFile(localDir.getStringCurrentDirectory() + "/"
                        + GUILocalTableView.getSelectionModel().getSelectedItem().getName());

            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Connection timed out. Retrying...");
                retryCount++;
                try {
                    Thread.sleep(2000); // Tunggu 2 detik sebelum mencoba kembali
                    remoteDir.open();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        TreeItem<FileInfo> selectedRemoteDir = new TreeItem<>(new FileInfo(
                GUILocalTableView.getSelectionModel().getSelectedItem().getName(),
                GUILocalTableView.getSelectionModel().getSelectedItem().getPath()));
        loadRemoteTableView(remoteDir.getStringDirectory(),
                selectedRemoteDir);
    }

    private void downloadFile(ActionEvent eveent) {
        remoteDir.downloadFile(GUIRemoteTableView.getSelectionModel().getSelectedItem().getName(),
                localDir.getStringCurrentDirectory());

        TreeItem<FileInfo> selectedLocalDir = new TreeItem<>(new FileInfo(
                GUIRemoteTableView.getSelectionModel().getSelectedItem().getName(),
                GUIRemoteTableView.getSelectionModel().getSelectedItem().getPath()));
        loadLocalTableView(localDir.getCurrentDirectory(), selectedLocalDir);
    }

    private void loadLocalDirectoryTable(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            TreeItem<FileInfo> selectedLocalDir = new TreeItem<>(new FileInfo(
                    GUILocalTableView.getSelectionModel().getSelectedItem().getName(),
                    GUILocalTableView.getSelectionModel().getSelectedItem().getPath()));
            localDir.changeDirecotry(selectedLocalDir.getValue().getPath());
            loadLocalTableView(localDir.getCurrentDirectory(), selectedLocalDir);
        }
    }

    private void loadRemoteDirectoryTable(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            TreeItem<FileInfo> selectedRemoteDir = new TreeItem<>(new FileInfo(
                    GUIRemoteTableView.getSelectionModel().getSelectedItem().getName(),
                    GUIRemoteTableView.getSelectionModel().getSelectedItem().getPath()));
            loadRemoteTableView(selectedRemoteDir.getValue().getPath(),
                    selectedRemoteDir);
        }
    }

    private void disconnectFTP(ActionEvent event) {
        try {
            this.app.showLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        remoteDir.close();
    }
}

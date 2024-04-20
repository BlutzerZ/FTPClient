package com.blutzerz.ftpclient.engine;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class LocalStorageEngine {
    private File localDir;

    public LocalStorageEngine(){
        FileSystemView view = FileSystemView.getFileSystemView();
        this.localDir = view.getHomeDirectory();
    }

    public void changeDirecotry(String path){
        // localDir = path;
    }

    public File getCurrentDirectory(){
        return localDir;
    }

    public String getContentDirectory(String path){
        // return localDir;
        return "";
    }
}

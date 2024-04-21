package com.blutzerz.ftpclient.engine;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class LocalStorageEngine {
    private File localDir;

    public LocalStorageEngine() {
        FileSystemView view = FileSystemView.getFileSystemView();
        this.localDir = view.getHomeDirectory();
    }

    public void changeDirecotry(String path) {
        localDir = new File(path);
    }

    public String getCurrentDirectory() {
        return localDir.getAbsolutePath();
    }

    public File[] getFilesDirectory() {
        File[] files = localDir.listFiles();
        return files;
    }

    public void deleteFile(String filename) {
        File file = new File(getCurrentDirectory() + "/" + filename);
        if (file.delete()) {
            System.out.println("File " + filename + " berhasil dihapus");
        } else {
            System.out.println("Gagal menghapus " + filename);
        }
    }
}

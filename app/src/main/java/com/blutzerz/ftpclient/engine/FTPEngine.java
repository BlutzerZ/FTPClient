package com.blutzerz.ftpclient.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPEngine {

    private String server;
    private int port;
    private String user;
    private String password;

    private String remoteDir = "/";

    private FTPClient ftpClient;

    // constructor
    public FTPEngine(String server, int port, String user, String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;

        ftpClient = new FTPClient();

        this.open();
    }

    public void open() {
        ftpClient.addProtocolCommandListener(
                (ProtocolCommandListener) new PrintCommandListener(new PrintStream(System.out)));

        try {
            ftpClient.connect(server, port);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new IOException("Exception in connecting to FTP Server");
            }

            boolean loggedIn = ftpClient.login(user, password);
            if (!loggedIn) {
                ftpClient.disconnect();
                throw new IOException("Failed to login to FTP Server");
            }

            ftpClient.changeWorkingDirectory(this.remoteDir);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean isConnected() {
        return ftpClient.isConnected();
    }

    public void changeDirectory(String path) {
        remoteDir = path;
        try {
            ftpClient.changeWorkingDirectory(remoteDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FTPFile[] getFilesDirectory() throws IOException {
        FTPFile[] files = null;
        files = ftpClient.listFiles(remoteDir);
        return files;
    }

    public String getStringDirectory() {
        return remoteDir;
    }

    public long getSize(String path) {
        long size = 0;
        try {
            size = ftpClient.mlistFile(path).getSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public void uploadFile(String localFilePath) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(localFilePath);
            fis = new FileInputStream(file);
            ftpClient.storeFile(remoteDir + "/" + file.getName(), fis);
            System.out.println("File berhasil diupload: " + localFilePath);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public void downloadFile(String filename, String localFolderPath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(localFolderPath + "/" + filename);
            ftpClient.retrieveFile(remoteDir + "/" + filename, fos);
            System.out.println("File berhasil didownload: " + localFolderPath + "/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteFile(String filename) throws IOException {
        boolean deleted = ftpClient.deleteFile(remoteDir + "/" + filename);
        if (deleted) {
            System.out.println("File berhasil dihapus: " + remoteDir + "/" + filename);
        } else {
            System.out.println("Gagal menghapus file: " + remoteDir + "/" + filename);
        }
    }

    public void close() {
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
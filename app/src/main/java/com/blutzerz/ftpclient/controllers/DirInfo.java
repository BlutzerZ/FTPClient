package com.blutzerz.ftpclient.controllers;

import java.text.SimpleDateFormat;

public class DirInfo {
    private String name;
    private String path;
    private String type;
    private String size;
    private String lastMod;

    public DirInfo(String name, String path, String type, long size, long lastMod) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = convertSizetoString(size);
        this.lastMod = convertLastModtoString(lastMod);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size.toString();
    }

    public String getLastMod() {
        return lastMod;
    }

    public String convertSizetoString(long size) {
        if (size > 1449616) {
            return (double) size / (1024 * 1024) + " mb";
        } else if (size > 1024) {
            return (double) size / 1024 + "  kb";
        } else {
            return size + " bytes";
        }
    }

    public String convertLastModtoString(long lastMod) {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(lastMod);
    }

    @Override
    public String toString() {
        return name;
    }
}

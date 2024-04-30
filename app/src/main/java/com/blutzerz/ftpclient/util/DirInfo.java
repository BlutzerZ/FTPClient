package com.blutzerz.ftpclient.util;

import java.text.SimpleDateFormat;

import javafx.scene.image.ImageView;

public class DirInfo {
    private ImageView icon;
    private String name;
    private String path;
    private String type;
    private String size;
    private String lastMod;

    public DirInfo(ImageView icon, String name, String path, String type, long size, long lastMod) {
        this.icon = icon;
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = convertSizetoString(size);
        this.lastMod = convertLastModtoString(lastMod);
    }

    public ImageView getIcon() {
        return icon;
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

package com.example.cs230;

public class AccessControlSettings {
    private boolean view;
    private boolean delete;
    private boolean download;

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }
}


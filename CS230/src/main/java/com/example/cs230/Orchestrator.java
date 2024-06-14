package com.example.cs230;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Orchestrator {
    private List<StorageServer> servers;
    private Database database;

    public Orchestrator(List<StorageServer> servers, Database database) {
        this.servers = servers;
        this.database = database;

        for (StorageServer server : servers) {
            List<String> serverFiles = server.listFiles();
            for (String fileName : serverFiles) {
                if (!database.fileExists(fileName)) {
                    database.updateFileInfo("unknown", fileName, "unknown", server.getServerId());
                }
            }
        }
    }

    public boolean uploadFile(String token, File file, String accessControl, AccessControlSettings settings) {
        if (!JwtUtil.validateToken(token)) {
            return false;
        }

        String username = JwtUtil.getUsernameFromToken(token);
        StorageServer server = chooseServer();
        if (server.uploadFile(file, accessControl)) {
            int fileId = database.updateFileInfo(username, file.getName(), accessControl, server.getServerId());
            int userId = database.getUserId(username);
            grantFullAccessToUploader(fileId, userId);
            updateFileAccessSettings(fileId, settings, username);
            return true;
        }
        return false;
    }

    private void grantFullAccessToUploader(int fileId, int userId) {
        database.grantAccess(fileId, userId, "read");
        database.grantAccess(fileId, userId, "download");
        database.grantAccess(fileId, userId, "delete");
    }

    private void updateFileAccessSettings(int fileId, AccessControlSettings settings, String uploaderUsername) {
        List<Integer> allUserIds = database.getAllUserIdsExcept(uploaderUsername);
        for (int otherUserId : allUserIds) {
            if (settings.isView()) {
                database.grantAccess(fileId, otherUserId, "read");
            }
            if (settings.isDelete()) {
                database.grantAccess(fileId, otherUserId, "delete");
            }
            if (settings.isDownload()) {
                database.grantAccess(fileId, otherUserId, "download");
            }
        }
    }

    public File downloadFile(String token, String fileName) {
        if (!JwtUtil.validateToken(token)) {
            return null;
        }

        String username = JwtUtil.getUsernameFromToken(token);
        if (!database.checkAccessRights(username, fileName, "download")) {
            return null;
        }

        for (StorageServer server : servers) {
            try {
                File file = server.downloadFile(fileName);
                if (file != null) {
                    return file;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean deleteFile(String token, String fileName) {
        if (!JwtUtil.validateToken(token)) {
            return false;
        }

        String username = JwtUtil.getUsernameFromToken(token);
        if (!database.checkAccessRights(username, fileName, "delete")) {
            return false;
        }

        boolean deleted = false;
        for (StorageServer server : servers) {
            if (server.deleteFile(fileName)) {
                deleted = true;
            }
        }
        if (deleted) {
            int fileId = database.getFileId(fileName);
            database.removeFileInfo(fileId);
        }
        return deleted;
    }

    public String[] listFiles(String token) {
        if (!JwtUtil.validateToken(token)) {
            return null;
        }

        String username = JwtUtil.getUsernameFromToken(token);
        List<String> files = new ArrayList<>();
        for (StorageServer server : servers) {
            files.addAll(server.listFiles());
        }
        return files.toArray(new String[0]);
    }

    private StorageServer chooseServer() {
        Random random = new Random();
        return servers.get(random.nextInt(servers.size()));
    }

    protected StorageServer getServerById(String serverId) {
        for (StorageServer server : servers) {
            if (server.getServerId().equals(serverId)) {
                return server;
            }
        }
        return null;
    }

    public Database getDatabase() {
        return database;
    }
}


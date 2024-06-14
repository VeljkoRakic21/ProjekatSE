package com.example.cs230;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageServer {
    private String serverId;
    private Map<String, String> fileStorage;
    private SecretKey secretKey;

    public StorageServer(String serverId) throws NoSuchAlgorithmException, IOException {
        this.serverId = serverId;
        this.fileStorage = new HashMap<>();
        this.secretKey = loadOrGenerateKey();

        File storageDir = new File(getStorageDir());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        loadExistingFiles();
    }

    private SecretKey loadOrGenerateKey() throws NoSuchAlgorithmException, IOException {
        if (KeyStorageUtil.keyExists(serverId)) {
            return KeyStorageUtil.loadKey(serverId);
        } else {
            SecretKey newKey = EncryptionUtil.generateKey();
            KeyStorageUtil.saveKey(serverId, newKey);
            return newKey;
        }
    }

    public String getServerId() {
        return serverId;
    }

    public boolean uploadFile(File file, String accessControl) {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            byte[] encryptedBytes = encrypt(fileBytes);
            Files.write(new File(getStoragePath(file.getName())).toPath(), encryptedBytes);
            fileStorage.put(file.getName(), accessControl);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public File downloadFile(String fileName) {
        try {
            byte[] encryptedBytes = Files.readAllBytes(new File(getStoragePath(fileName)).toPath());
            byte[] decryptedBytes = decrypt(encryptedBytes);
            File tempFile = File.createTempFile(fileName, null);
            Files.write(tempFile.toPath(), decryptedBytes);
            return tempFile;
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + fileName);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(getStoragePath(fileName)));
            fileStorage.remove(fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> listFiles() {
        return new ArrayList<>(fileStorage.keySet());
    }

    private byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during encryption", e);
        }
    }

    private byte[] decrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during decryption", e);
        }
    }

    private String getStorageDir() {
        return "storage/" + serverId;
    }

    private String getStoragePath(String fileName) {
        return getStorageDir() + "/" + fileName;
    }

    private void loadExistingFiles() {
        File dir = new File(getStorageDir());
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                fileStorage.put(file.getName(), "unknown");
            }
        }
    }
}

package com.example.cs230;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KeyStorageUtil {
    private static final String KEY_STORAGE_PATH = "keys/";

    public static void saveKey(String serverId, SecretKey key) throws IOException {
        String encodedKey = EncryptionUtil.encodeKey(key);
        Files.createDirectories(Paths.get(KEY_STORAGE_PATH));
        Files.write(Paths.get(KEY_STORAGE_PATH + serverId + ".key"), encodedKey.getBytes());
    }

    public static SecretKey loadKey(String serverId) throws IOException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(KEY_STORAGE_PATH + serverId + ".key"));
        String encodedKey = new String(keyBytes);
        return EncryptionUtil.decodeKey(encodedKey);
    }

    public static boolean keyExists(String serverId) {
        return Files.exists(Paths.get(KEY_STORAGE_PATH + serverId + ".key"));
    }
}

package com.example.cs230;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE_128 = 128;
    private static final int KEY_SIZE_256 = 256;

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        return generateKey(KEY_SIZE_128);
    }

    public static SecretKey generate256BitKey() throws NoSuchAlgorithmException {
        return generateKey(KEY_SIZE_256);
    }

    private static SecretKey generateKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    public static String encodeKey(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static SecretKey decodeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }
}

package com.example.cs230;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorUtil {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        SecretKey secretKey = EncryptionUtil.generate256BitKey();
        String encodedKey = EncryptionUtil.encodeKey(secretKey);
        System.out.println("Base64 Encoded Key: " + encodedKey);
    }
}




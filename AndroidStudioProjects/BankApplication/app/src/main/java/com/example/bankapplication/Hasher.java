package com.example.bankapplication;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Hasher {

    public static String getRandomSalt() {
        Random rng = new SecureRandom();
        byte[] salt = new byte[16];
        rng.nextBytes(salt);
        return salt.toString();
    }

    public static String hashPassword(String password, String salt) {
        String hashPass = password + salt;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            //digest.update(salt);
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("_LOG: "+e);
            return null;
        }
        byte[] bytes = digest.digest(hashPass.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static boolean testPassword(String password, String hashedPassword, String salt) {
        if (hashPassword(password, salt).equals(hashedPassword))
            return true;
        else
            return false;
    }
}

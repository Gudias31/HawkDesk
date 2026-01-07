package com.hawkdesk.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
    
    /**
     * Hash a password using SHA-256
     * @param password Plain text password
     * @return Hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify if a password matches a hash
     * @param password Plain text password
     * @param hashedPassword Hashed password to compare
     * @return true if password matches hash
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String hashToCheck = hashPassword(password);
        return hashToCheck.equals(hashedPassword);
    }
}

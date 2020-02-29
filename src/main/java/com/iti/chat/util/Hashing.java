package com.iti.chat.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class Hashing {

    private static final SecureRandom RAND = new SecureRandom();

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final String mySalt = "iTyUFs+WRmVProEQlHYu+1+CTcAZt153yHTkWd3sheMGTt+7stYCQPKxo7W/Ul7bQDvpKJPvSKfBiuGnFBQ7KFkZFhugpuMghxMA+emrwr/nfPbgYNCRUjAxuTjYmRIV6PfJmj2nAsUvgVk0pqQYFp1Z1XW2cN3XuLMXdUyxmop9NA2xgoI47d9xESrS2qthKvVbIC/smIlzq508D8VqN5qMqF9nq7wefULtP2TOIi0DUfSyPLuaVXyBiW4uN8g7l+Mnrk3FqR1/2SoEj65XJFxP9rYnhmIsuoaZ47M0rbqIRSStVW9VGi9SOPIIjhqLK0HOKemMR8QU7SRfdAST3wHNyAjnjghco0xIL9AHhkgdOFbo3sn+qyKa0YLAVUHWFX8bOXgpWFM6aWB1XhX49zCwyHVEKY3wXoW4sVZGeuyHcuU+ZCaWAs+lra";

    private static Optional<String> generateSalt(final int length) {

        if (length < 1) {
            System.err.println("error in generateSalt: length must be > 0");
            return Optional.empty();
        }

        byte[] salt = new byte[length];
        RAND.nextBytes(salt);

        return Optional.of(Base64.getEncoder().encodeToString(salt));
    }

    private static Optional<String> hashPassword(String password, String salt) {

        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return Optional.empty();

        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verifyPassword(String password, String key) {
        Optional<String> optEncrypted = hashPassword(password, mySalt);
        if (!optEncrypted.isPresent()) {
            return false;
        }
        return optEncrypted.get().equals(key);
    }

    public static String getSecurePassword(String password) {
//        String salt = generateSalt(512).get();
        return hashPassword(password, mySalt).get();
    }

}
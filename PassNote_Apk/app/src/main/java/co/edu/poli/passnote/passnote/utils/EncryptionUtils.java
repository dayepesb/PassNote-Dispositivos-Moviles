package co.edu.poli.passnote.passnote.utils;

import android.util.Base64;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;

public class EncryptionUtils {

    public static final String SEED = "PassNote Rules!!!";

    public static String encrypt(String clearText) {
        String result = null;
        try {
            Cipher c = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            c.init(Cipher.ENCRYPT_MODE, getKey());
            byte[] encodedBytes = c.doFinal(clearText.getBytes());
            result = new String(Base64.encode(encodedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            showGeneralError();
        }
        return result;
    }

    public static String decrypt(String encryptedText) {
        String result = null;
        try {
            Cipher c = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            c.init(Cipher.DECRYPT_MODE, getKey());
            byte[] decodedBytes = c.doFinal(Base64.decode(encryptedText, Base64.DEFAULT));
            result = new String(decodedBytes);
        } catch (Exception e) {
            showGeneralError();
        }
        return result;
    }

    private static SecretKeySpec getKey() throws Exception {
        byte[] key = (SEED).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}

package co.edu.poli.passnote.passnote.utils;

import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

    private static final String TAG = EncryptionUtils.class.getName();
    private static final String PASSNOTE_ENCRYPTION_SEED = "PassNote rules!!!!";
    private static final int PASSNOTE_ENCRYPTION_KEYSIZE = 128;

    public static String encrypt(String plainTextPassword) {
        String encryptedPassword = null;
        try {
            byte[] passwordBytes = plainTextPassword.getBytes();
            byte[] seedBytes = PASSNOTE_ENCRYPTION_SEED.getBytes();

            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seedBytes);

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(PASSNOTE_ENCRYPTION_KEYSIZE, secureRandom);

            SecretKey secretKey = keyGenerator.generateKey();

            byte[] key = secretKey.getEncoded();
            byte[] encryptedPasswordBytes = encrypt(key, passwordBytes);
            encryptedPassword = new String(new Base64().encode(encryptedPasswordBytes),
                    Charset.defaultCharset());
        } catch (Exception e) {
            Log.e(TAG, "Error during password encryption", e);
        }
        return encryptedPassword;
    }

    public static String decrypt(String encryptedPassword) {
        String decryptedPassword = null;
        try {
            byte[] encryptedPasswordBytes = new Base64().decode(encryptedPassword);
            byte[] seedBytes = PASSNOTE_ENCRYPTION_SEED.getBytes();

            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seedBytes);

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(PASSNOTE_ENCRYPTION_KEYSIZE, secureRandom);

            SecretKey secretKey = keyGenerator.generateKey();
            byte[] key = secretKey.getEncoded();

            byte[] decryptedPasswordBytes = decrypt(key, encryptedPasswordBytes);
            decryptedPassword = new String(decryptedPasswordBytes, Charset.defaultCharset());
        } catch (Exception e) {
            Log.e(TAG, "Error decrypting password", e);
        }
        return decryptedPassword;
    }


    private static byte[] encrypt(byte[] keyBytes, byte[] plainTextDataBytes) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(plainTextDataBytes);
        return encrypted;
    }

    private static byte[] decrypt(byte[] keyBytes, byte[] encryptedDataBytes) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decrypted = cipher.doFinal(encryptedDataBytes);
        return decrypted;
    }
}

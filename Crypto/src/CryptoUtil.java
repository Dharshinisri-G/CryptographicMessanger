import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtil {
    private static final String ALGORITHM = "AES"; // You can use any encryption algorithm (AES, RSA, etc.)

    // Encrypt a plain text message
    public static String encrypt(String plainText, String secret) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Decrypt an encrypted message
    public static String decrypt(String encryptedText, String secret) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decrypted);
    }
}

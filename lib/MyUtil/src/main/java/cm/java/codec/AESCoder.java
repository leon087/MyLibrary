package cm.java.codec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESCoder {

    private static final int KEY_LENGTH = 16;

    public static final String ALG_AES = "AES";

    public static final String C_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

    /**
     * 需要BC支持<br>
     * BouncyCastleProvider provider = new BouncyCastleProvider(); <br>
     * Security.addProvider(provider);
     */
    public static final String C_AES_GCM = "AES/GCM/NoPadding";

    /**
     * BouncyCastleProvider
     */
    public static final String PROVIDER_BC = "BC";

    private AESCoder() {
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        return generateKey(KEY_LENGTH);
    }

    public static SecretKey generateKey(int keyLength) throws NoSuchAlgorithmException {
        final SecureRandom random = new SecureRandom();

        final KeyGenerator generator = KeyGenerator.getInstance(ALG_AES);

        int keySizeBit = SecureUtil.convertSize(keyLength);
        generator.init(keySizeBit, random);

        return generator.generateKey();
    }

    public static SecretKey generateKey(char[] password, byte[] salt, int keyLength)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecretKey tmp = HashUtil.generateHash(password, salt, keyLength);
        SecretKey secret = getSecretKey(tmp.getEncoded());
        return secret;
    }

    public static SecretKey getSecretKey(byte[] key) {
        SecretKey secret = new SecretKeySpec(key, ALG_AES);
        return secret;
    }

    public static byte[] encrypt(byte[] key, byte[] iv, byte[] src) throws Exception {
        SecretKey secret = getSecretKey(key);
        return encrypt(secret, iv, src);
    }

    public static byte[] decrypt(byte[] key, byte[] iv, byte[] encrypted) throws Exception {
        SecretKey secret = getSecretKey(key);
        return decrypt(secret, iv, encrypted);
    }

    public static byte[] encrypt(SecretKey secretKey, byte[] iv, byte[] src) throws Exception {
        return encrypt(C_AES_CBC_PKCS5PADDING, secretKey, iv, null, src);
    }

    public static byte[] decrypt(SecretKey secretKey, byte[] iv, byte[] src) throws Exception {
        return decrypt(C_AES_CBC_PKCS5PADDING, secretKey, iv, null, src);
    }

    public static byte[] encrypt(String transformation, SecretKey secretKey, byte[] iv, byte[] aad, byte[] src) throws Exception {
        return doFinal(transformation, Cipher.ENCRYPT_MODE, secretKey, iv, aad, src);
    }

    public static byte[] decrypt(String transformation, SecretKey secretKey, byte[] iv, byte[] aad, byte[] src) throws Exception {
        return doFinal(transformation, Cipher.DECRYPT_MODE, secretKey, iv, aad, src);
    }

    private static byte[] doFinal(String transformation,
                                  int opmode,
                                  SecretKey secretKey,
                                  byte[] iv,
                                  byte[] aad,
                                  byte[] src)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        IvParameterSpec ivSpec = getIv(iv);

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(opmode, secretKey, ivSpec);
        if (aad != null) {
            cipher.updateAAD(aad);
        }
        byte[] dest = cipher.doFinal(src);
        return dest;
    }

    private static IvParameterSpec getIv(byte[] iv) {
        if (iv == null) {
            return null;
        } else {
            return new IvParameterSpec(iv);
        }
    }

}

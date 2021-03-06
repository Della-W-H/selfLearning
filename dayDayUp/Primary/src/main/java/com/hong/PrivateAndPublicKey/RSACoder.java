package com.hong.PrivateAndPublicKey;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSACoder {
    private final static String KEY_ALGORITHM = "RSA";
    private KeyPair keyPair;

    public RSACoder() throws Exception {
        this.keyPair = initKeyPair();
    }

    public byte[] encryptByPublicKey(byte[] plaintext, byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plaintext);
    }

    public byte[] encryptByPrivateKey(byte[] plaintext, byte[] key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(plaintext);
    }

    public byte[] decryptByPublicKey(byte[] ciphertext, byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(ciphertext);
    }

    public byte[] decryptByPrivateKey(byte[] ciphertext, byte[] key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(ciphertext);
    }

    private KeyPair initKeyPair() throws Exception {
        // KeyPairGenerator??????????????????????????????????????????RSA??????????????????
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // ?????????????????????????????????????????????1024???
        keyPairGen.initialize(1024);
        // ?????????????????????
        return keyPairGen.genKeyPair();
    }

    public static void main(String[] args) throws Exception {
        String msg = "Hello World!";
        RSACoder coder = new RSACoder();
        // ???????????????????????????
        byte[] ciphertext = coder.encryptByPrivateKey(msg.getBytes("UTF8"), coder.keyPair.getPrivate().getEncoded());
        byte[] plaintext = coder.decryptByPublicKey(ciphertext, coder.keyPair.getPublic().getEncoded());

        // ???????????????????????????
        byte[] ciphertext2 = coder.encryptByPublicKey(msg.getBytes(), coder.keyPair.getPublic().getEncoded());
        byte[] plaintext2 = coder.decryptByPrivateKey(ciphertext2, coder.keyPair.getPrivate().getEncoded());

        System.out.println("?????????" + msg);
        System.out.println("?????????" + Base64.encodeBase64URLSafeString(coder.keyPair.getPublic().getEncoded()));
        System.out.println("?????????" + Base64.encodeBase64URLSafeString(coder.keyPair.getPrivate().getEncoded()));

        System.out.println("============== ??????????????????????????? ==============");
        System.out.println("?????????" + Base64.encodeBase64URLSafeString(ciphertext));
        System.out.println("?????????" + new String(plaintext));

        System.out.println("============== ??????????????????????????? ==============");
        System.out.println("?????????" + Base64.encodeBase64URLSafeString(ciphertext2));
        System.out.println("?????????" + new String(plaintext2));
    }
}
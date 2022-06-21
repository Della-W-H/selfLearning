package com.hong.PrivateAndPublicKey;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * @author wanghong
 * @date 2022/6/3
 * @apiNote
 */
public class RSA {
    private static String src = "imooc security rsa";

    public static void main(String[] args) {
        jdkRSA();
    }

    public static void jdkRSA() {
        try {
            // 1 初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

            // 2 私钥加密、公钥解密——加密
            // 用私钥进行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 执行key的转化
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            // 声明一个签名对象 使用JDK实现
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] sign = signature.sign();
            System.out.println("私钥加密、公钥解密——加密:" + Hex.encodeHexString(sign));

            // 3私钥加密、公钥解密——解密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            // 创建签名对象
            signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean verify = signature.verify(sign);
            System.out.println("私钥加密、公钥解密——解密:" + verify);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

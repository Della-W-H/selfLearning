package com.tuling.shiroAuth.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class MyPassWordEncoder {

    public static String getEncodedPassword(String password){
        SimpleHash simpleHash = new SimpleHash(MyConstants.PASS_ALG,password, MyConstants.PASS_SALT,MyConstants.PASS_HASH_ITER);
        return simpleHash.toString();
    }
}

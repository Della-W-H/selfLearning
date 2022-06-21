package com.tuling.shiroAuth.utils;

import org.apache.shiro.util.ByteSource;

public class MyConstants {

    public static final String PASS_ALG = "MD5";
    public static final ByteSource PASS_SALT = ByteSource.Util.bytes("salt");
    public static final int PASS_HASH_ITER = 2;
}

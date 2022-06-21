package com.tuling;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class CredentialsMatcherTest {

    @Resource
    private CredentialsMatcher matcher;

    @Test
    public void getEncoderedPass(){
        String password = "manager";
        SimpleHash simpleHash = new SimpleHash("MD5",password,ByteSource.Util.bytes("salt"),2);
        System.out.println(simpleHash);
    }
}

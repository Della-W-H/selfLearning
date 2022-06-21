package com.hong.PrivateAndPublicKey;


//注意这个类 JDK11无法被引入项目
import javax.xml.bind.DatatypeConverter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author wanghong
 * @date 2022/6/3
 * @apiNote  https://blog.csdn.net/pmdream/article/details/101030476?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522165424355016782391856635%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=165424355016782391856635&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~baidu_landing_v2~default-1-101030476-null-null.142^v11^pc_search_result_control_group,157^v13^control&utm_term=https%3A%2F%2Fblog.csdn.net%2Fmn960mn%2Farticle%2Fdetails%2F78174234&spm=1018.2226.3001.4187
 */
public class SignUtilsMac {
    public static void main(String[] args) {


    }

    private static String sign(String secretKey, String sigStr) throws Exception {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), mac.getAlgorithm());
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(sigStr.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printBase64Binary(hash);
        } catch (Exception e) {
            throw new Exception(e.getClass().getName() + "-" + e.getMessage());
        }

    }
}

package com.yz.gulimall.thirdparty.component;

import com.yz.gulimall.thirdparty.util.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Data
@Component
public class SmsComponent {

    public static void sendSmsCode(String phone, String code) throws IOException {
        System.out.println("phone:"+phone+": code:"+code);
        File smsCodeDir = new File("C:\\Users\\风灵\\Desktop\\smscode");
        if(!smsCodeDir.exists()){// 如果C:\Users\风灵\Desktop\smscode不存在，就创建为目录
            smsCodeDir.mkdir();
        }
        // 创建以dir2为父目录,名为"test.txt"的File对象
        File smscode = new File(smsCodeDir,"code.txt");
        if(!smscode.exists()){// 如果还不存在，就创建为文件
            smscode.createNewFile();
        }
        OutputStream outputStream= new FileOutputStream(smscode);
        outputStream.write(code.getBytes());
        outputStream.flush();
        outputStream.close();
    }


    /*public static void main(String[] args) {
        String host = "https://hcapi23.market.alicloudapi.com";
        String path = "/sms/send";
        String method = "POST";
        String appcode = "45d4fc517b8f460dbe40d3524625b96b";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("phone_numbers", "18275015906");
        *//**
         * sign_name：需要找客服拿到签名名称
         *//*
        bodys.put("sign_name", "162123");
        bodys.put("template_code", "6750");
        bodys.put("template_param", "你的验证码是：");


        try {
            *//**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             *//*
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}

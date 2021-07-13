package com.yz.gulimall.thirdparty.controller;

import com.yz.common.utils.R;
import com.yz.gulimall.thirdparty.component.SmsComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/sms")
public class SmsSendController {

    //TODO 没有使用短信服务功能，这是自定义的一个短信测试工具类
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone")String phone,@RequestParam("code")String code) throws IOException {
        SmsComponent.sendSmsCode(phone,code);
        return R.ok();
    }


}

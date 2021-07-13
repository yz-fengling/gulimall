package com.yz.gulimall.member.exception;

import com.yz.common.exception.BizCodeEnum;

public class PhoneExsitException extends RuntimeException {
    public PhoneExsitException() {
        super(BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
    }
}

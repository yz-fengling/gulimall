package com.yz.gulimall.member.exception;

import com.yz.common.exception.BizCodeEnum;

public class UsernameExistExcepetion extends RuntimeException {
    public UsernameExistExcepetion() {
        super(BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
    }
}

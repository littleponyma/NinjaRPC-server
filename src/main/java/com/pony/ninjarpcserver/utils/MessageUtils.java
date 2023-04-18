package com.pony.ninjarpcserver.utils;

import com.alibaba.fastjson.JSON;
import com.pony.ninjarpcserver.model.BaseResponse;

public class MessageUtils {
    public static String sendErroMsg(int code, String msg) {
        BaseResponse baseResponse = new BaseResponse(code, msg, null);
        return JSON.toJSONString(baseResponse);
    }
}

package com.pony.ninjarpc.model;

import com.alibaba.fastjson.JSON;

public class Result {

    public static String success() {
        return "{}";
    }

    public static String parameterErro() {
        BaseResponse baseResponse = new BaseResponse(-1001, "参数为空", null);
        return JSON.toJSONString(baseResponse);
    }

    public static String timeoutErro() {
        BaseResponse baseResponse = new BaseResponse(-1002, "请求超时请重试", null);
        return JSON.toJSONString(baseResponse);
    }

    public static String noneDevice() {
        BaseResponse baseResponse = new BaseResponse(-1003, "暂无空闲设备", null);
        return JSON.toJSONString(baseResponse);
    }
}

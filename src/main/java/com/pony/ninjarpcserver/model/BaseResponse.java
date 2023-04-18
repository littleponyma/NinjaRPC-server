package com.pony.ninjarpcserver.model;

import lombok.Data;

@Data
public class BaseResponse {
    private int code;
    private String msg;
    private Object data;

    public BaseResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}

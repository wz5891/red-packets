package com.example.redpackets.enums;

import lombok.Getter;

public enum StatusCode {
    SUCCESS(0, "成功"),
    FAIL(-1, "失败"),
    INVALID_PARAMS(201, "非法的参数");

    @Getter
    private Integer code;
    @Getter
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

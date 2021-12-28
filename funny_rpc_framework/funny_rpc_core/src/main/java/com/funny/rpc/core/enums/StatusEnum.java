package com.funny.rpc.core.enums;

import lombok.Getter;

/**
 * @author frankq
 * @date 2021/12/28
 */
@Getter
public enum StatusEnum {

    SUCCESS(200, "OK"),

    NOT_FOUND_SERVICE_PROVIDER(100001, "not found service provider");

    private Integer code;
    private String description;

    StatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

}

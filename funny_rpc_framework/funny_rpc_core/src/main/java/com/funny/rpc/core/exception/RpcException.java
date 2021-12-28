package com.funny.rpc.core.exception;

import com.funny.rpc.core.enums.StatusEnum;

/**
 *
 * RPC框架异常信息
 *
 * @author frankq
 * @date 2021/12/28
 */
public class RpcException extends RuntimeException {

    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(StatusEnum statusEnum) {
        super(statusEnum.getDescription());
    }

}

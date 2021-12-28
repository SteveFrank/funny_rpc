package com.funny.rpc.core.utils;

public class RequestIdUtil {

    /**
     * 请求ID的生成还需要完善
     * @return
     */
    public static String requestId() {
        return GlobalIDGenerator.getInstance().nextStrId();
    }

}

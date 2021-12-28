package com.funny.rpc.core.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Rpc 应答的参数封装
 *
 * @author frankq
 * @date 2021/12/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse {
    /**
     * 每次请求的唯一ID
     */
    private String requestId;
    /**
     * 返回的数据结果
     */
    private Object result;
    /**
     * 异常相关处理
     */
    private Throwable cause;

    public boolean isError() {
        return cause != null;
    }

}

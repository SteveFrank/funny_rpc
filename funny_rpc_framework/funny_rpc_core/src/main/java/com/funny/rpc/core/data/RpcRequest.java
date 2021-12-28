package com.funny.rpc.core.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Rpc 请求的数据封装
 *
 * @author frankq
 * @date 2021/12/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest {
    /**
     * 每次请求的唯一ID
     */
    private String requestId;
    /**
     * 调用的哪一个接口
     */
    private String className;
    /**
     * 调用的接口的方法
     */
    private String methodName;
    /**
     * 方法的参数类型
     * 注意保证参数类型与参数的一致
     */
    private Class<?>[] parameterTypes;
    /**
     * 具体的参数
     */
    private Object[] parameters;
}

package com.funny.rpc.core.netty.request;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;


public class RequestPromise  extends DefaultPromise {

    public RequestPromise(EventExecutor executor) {
        super(executor);
    }
}

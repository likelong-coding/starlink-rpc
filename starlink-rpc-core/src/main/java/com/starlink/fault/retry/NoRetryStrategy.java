package com.starlink.fault.retry;

import com.starlink.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * 不重试 - 重试策略
 */
@Slf4j
public class NoRetryStrategy implements RetryStrategy {

    /**
     * 重试
     */
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }

}
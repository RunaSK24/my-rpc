package cn.edu.szu.myrpc.fault.retry;

import cn.edu.szu.myrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 */
public interface RetryStrategy {

    /**
     * 重试
     * @param callable 回调
     * @return 重试结果
     * @throws Exception 异常
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}

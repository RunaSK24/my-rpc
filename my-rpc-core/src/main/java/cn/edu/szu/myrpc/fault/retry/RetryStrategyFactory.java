package cn.edu.szu.myrpc.fault.retry;

import cn.edu.szu.myrpc.spi.SpiLoader;

/**
 * 重试策略工厂
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 获取实例
     * @param key 键值
     * @return 对应重试策略
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}

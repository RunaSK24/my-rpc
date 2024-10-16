package cn.edu.szu.myrpc.fault.tolerant;

import cn.edu.szu.myrpc.spi.SpiLoader;

/**
 * 容错策略工厂
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 获取实例
     * @param key 容错策略键值
     * @return 指定容错策略
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }

}

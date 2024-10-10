package cn.edu.szu.myrpc.loadbalancer;

import cn.edu.szu.myrpc.spi.SpiLoader;

/**
 * 负载均衡器工厂
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 获取实例
     * @param key 键名
     * @return 实例对象
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}

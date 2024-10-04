package cn.edu.szu.myrpc.registry;

import cn.edu.szu.myrpc.spi.SpiLoader;

public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 获取注册中心实例
     * @param key 注册中心标识
     * @return 注册中心实例
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}

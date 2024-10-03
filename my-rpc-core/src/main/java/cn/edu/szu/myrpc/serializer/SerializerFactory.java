package cn.edu.szu.myrpc.serializer;

import cn.edu.szu.myrpc.spi.SpiLoader;

/**
 * 序列化器工厂
 */
public class SerializerFactory {
    // 序列化器映射列表
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 静态工厂方法，获取序列化器实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
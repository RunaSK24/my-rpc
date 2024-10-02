package cn.edu.szu.myrpc.serializer;

import java.io.IOException;

/**
 * 序列化器接口
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param object 要序列化的对象
     * @param <T> 对象类型
     * @return 序列化后的字节数组
     * @throws IOException IO异常
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes 要反序列化的字节数组
     * @param type 对象类型
     * @param <T> 对象类型
     * @return 反序列化后的对象
     * @throws IOException IO异常
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}


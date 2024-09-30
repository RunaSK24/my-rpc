package cn.edu.szu.myrpc.proxy;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂
 */
public class ServiceProxyFactory {
    /**
     * 获取代理对象
     * @param serviceClass 服务类
     * @return 代理对象
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}

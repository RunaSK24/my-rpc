package cn.edu.szu.myrpc.proxy;

import cn.edu.szu.myrpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂
 */
public class ServiceProxyFactory {
    /**
     * 获取代理对象
     * @param serviceClass 服务类类名
     * @return 代理对象
     * @param <T> 服务类
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        // 判断配置文件中是否开启mock
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        // 返回代理对象
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    /**
     * 获取 Mock 代理对象
     * @param serviceClass 服务类类名
     * @return 代理对象
     * @param <T> 服务类
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}

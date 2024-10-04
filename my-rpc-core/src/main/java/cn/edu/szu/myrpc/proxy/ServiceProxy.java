package cn.edu.szu.myrpc.proxy;

import cn.edu.szu.myrpc.RpcApplication;
import cn.edu.szu.myrpc.config.RpcConfig;
import cn.edu.szu.myrpc.constant.RpcConstant;
import cn.edu.szu.myrpc.model.RpcRequest;
import cn.edu.szu.myrpc.model.RpcResponse;
import cn.edu.szu.myrpc.model.ServiceMetaInfo;
import cn.edu.szu.myrpc.registry.Registry;
import cn.edu.szu.myrpc.registry.RegistryFactory;
import cn.edu.szu.myrpc.serializer.Serializer;
import cn.edu.szu.myrpc.serializer.SerializerFactory;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务代理
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 获取RPC配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 从工厂取得序列化器
        Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());

        // 取得注册中心实例
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());

        // 构建ServiceMetaInfo对象，通过ServiceMetaInfo中的getServiceKey方法拼接服务键名
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(method.getDeclaringClass().getName());
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        String serviceKey = serviceMetaInfo.getServiceKey();
        // 通过注册中心中取得服务列表
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        // TODO：负载均衡，这里暂时先取第一个服务
        ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

        // 构建RPC请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // 序列化RPC请求
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;

            // 通过HTTP发送请求
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }

            // 反序列化PRC应答并返回
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

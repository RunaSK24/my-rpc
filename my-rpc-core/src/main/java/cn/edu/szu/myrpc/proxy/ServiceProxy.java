package cn.edu.szu.myrpc.proxy;

import cn.edu.szu.myrpc.RpcApplication;
import cn.edu.szu.myrpc.config.RpcConfig;
import cn.edu.szu.myrpc.model.RpcRequest;
import cn.edu.szu.myrpc.model.RpcResponse;
import cn.edu.szu.myrpc.serializer.JdkSerializer;
import cn.edu.szu.myrpc.serializer.Serializer;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 创建序列化器
        Serializer serializer = new JdkSerializer();

        // 获取目的地址
        StringBuilder address = new StringBuilder("http://");
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        address.append(rpcConfig.getServerHost()).append(":").append(rpcConfig.getServerPort());

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
            try (HttpResponse httpResponse = HttpRequest.post(address.toString())
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

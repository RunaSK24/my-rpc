package cn.edu.szu.myrpc.proxy;

import cn.edu.szu.myrpc.model.RpcRequest;
import cn.edu.szu.myrpc.model.RpcResponse;
import cn.edu.szu.myrpc.serializer.JdkSerializer;
import cn.edu.szu.myrpc.serializer.Serializer;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 创建序列化器
        Serializer serializer = new JdkSerializer();

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
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
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

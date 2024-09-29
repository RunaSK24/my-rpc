package cn.edu.szu.example.consumer;

import cn.edu.szu.example.common.model.User;
import cn.edu.szu.example.common.service.UserService;
import cn.edu.szu.myrpc.model.RpcRequest;
import cn.edu.szu.myrpc.model.RpcResponse;
import cn.edu.szu.myrpc.serializer.JdkSerializer;
import cn.edu.szu.myrpc.serializer.Serializer;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.io.IOException;

/**
 * UserService静态代理类（灵活性较差，不推荐使用）
 */
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        // 创建序列化器
        Serializer serializer = new JdkSerializer();

        // 构建RPC请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
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
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

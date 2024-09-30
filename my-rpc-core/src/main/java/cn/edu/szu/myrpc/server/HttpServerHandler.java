package cn.edu.szu.myrpc.server;

import cn.edu.szu.myrpc.model.RpcRequest;
import cn.edu.szu.myrpc.model.RpcResponse;
import cn.edu.szu.myrpc.registry.LocalRegistry;
import cn.edu.szu.myrpc.serializer.JdkSerializer;
import cn.edu.szu.myrpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        final Serializer serializer = new JdkSerializer();

        // 记录接收请求日志
        System.out.println("Received request: " + request.method() + " " + request.uri());

        // 异步处理HTTP请求
        request.bodyHandler(body -> {
            // 解析请求体
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 构造响应对象
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                // RPC请求为空，响应错误信息
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                // 利用反射机制创建对象并执行方法
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                // 封装返回的rpc应答对象
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("success");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 响应执行结果
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应HTTP请求
     * @param request HTTP请求
     * @param rpcResponse RPC响应内容
     * @param serializer 序列化器
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}

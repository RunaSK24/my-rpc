package cn.edu.szu.myrpc.proxy;

import cn.edu.szu.myrpc.RpcApplication;
import cn.edu.szu.myrpc.config.RpcConfig;
import cn.edu.szu.myrpc.constant.RpcConstant;
import cn.edu.szu.myrpc.loadbalancer.LoadBalancer;
import cn.edu.szu.myrpc.loadbalancer.LoadBalancerFactory;
import cn.edu.szu.myrpc.model.RpcRequest;
import cn.edu.szu.myrpc.model.RpcResponse;
import cn.edu.szu.myrpc.model.ServiceMetaInfo;
import cn.edu.szu.myrpc.registry.Registry;
import cn.edu.szu.myrpc.registry.RegistryFactory;
import cn.edu.szu.myrpc.server.tcp.VertxTcpClient;
import cn.hutool.core.collection.CollUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 服务代理
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws ExecutionException, InterruptedException {
        // 获取RPC配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

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

        // 构建RPC请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        // 通过TCP客户端发送RPC请求并
        RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
        return rpcResponse.getData();
    }
}

package cn.edu.szu.example.provider;

import cn.edu.szu.example.common.service.UserService;
import cn.edu.szu.myrpc.RpcApplication;
import cn.edu.szu.myrpc.config.RegistryConfig;
import cn.edu.szu.myrpc.config.RpcConfig;
import cn.edu.szu.myrpc.model.ServiceMetaInfo;
import cn.edu.szu.myrpc.registry.LocalRegistry;
import cn.edu.szu.myrpc.registry.Registry;
import cn.edu.szu.myrpc.registry.RegistryFactory;
import cn.edu.szu.myrpc.server.Server;
import cn.edu.szu.myrpc.server.tcp.VertxTcpServer;

public class ProviderExample2 {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 取得RPC配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 在本地注册UserService服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 根据配置取得注册中心实例
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        // 注册当前服务到注册中心
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动服务
        Server server = new VertxTcpServer();
        server.doStart(rpcConfig.getServerPort());
    }
}

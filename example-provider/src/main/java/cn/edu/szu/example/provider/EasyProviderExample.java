package cn.edu.szu.example.provider;

import cn.edu.szu.example.common.service.UserService;
import cn.edu.szu.myrpc.RpcApplication;
import cn.edu.szu.myrpc.registry.LocalRegistry;
import cn.edu.szu.myrpc.server.Server;
import cn.edu.szu.myrpc.server.VertxHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动服务
        Server server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}

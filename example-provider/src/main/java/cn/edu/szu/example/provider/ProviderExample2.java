package cn.edu.szu.example.provider;

import cn.edu.szu.example.common.service.UserService;
import cn.edu.szu.myrpc.RpcApplication;
import cn.edu.szu.myrpc.bootstrap.ProviderBootstrap;
import cn.edu.szu.myrpc.config.RegistryConfig;
import cn.edu.szu.myrpc.config.RpcConfig;
import cn.edu.szu.myrpc.model.ServiceMetaInfo;
import cn.edu.szu.myrpc.model.ServiceRegisterInfo;
import cn.edu.szu.myrpc.registry.LocalRegistry;
import cn.edu.szu.myrpc.registry.Registry;
import cn.edu.szu.myrpc.registry.RegistryFactory;
import cn.edu.szu.myrpc.server.Server;
import cn.edu.szu.myrpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

public class ProviderExample2 {
    public static void main(String[] args) {
        // 构造注册服务列表
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserServiceImpl> userServiceInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(userServiceInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}

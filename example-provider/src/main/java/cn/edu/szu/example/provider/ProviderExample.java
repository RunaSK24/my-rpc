package cn.edu.szu.example.provider;

import cn.edu.szu.example.common.service.UserService;
import cn.edu.szu.myrpc.bootstrap.ProviderBootstrap;
import cn.edu.szu.myrpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

public class ProviderExample {
    public static void main(String[] args) {
        // 构造注册服务列表
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserServiceImpl> userServiceInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(userServiceInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}

package cn.edu.szu.example.consumer;

import cn.edu.szu.example.common.model.User;
import cn.edu.szu.example.common.service.UserService;
import cn.edu.szu.myrpc.proxy.ServiceProxyFactory;

public class ConsumerExample {
    public static void main(String[] args) {
        // 获取代理对象
        UserService userService = ServiceProxyFactory.getMockProxy(UserService.class);

        // 进行RPC
        User user = new User();
        user.setName("朱国荣");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        float number = userService.getNumber();
        System.out.println(number);
    }
}

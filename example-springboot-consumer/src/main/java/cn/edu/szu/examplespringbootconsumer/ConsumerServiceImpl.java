package cn.edu.szu.examplespringbootconsumer;

import cn.edu.szu.example.common.model.User;
import cn.edu.szu.example.common.service.UserService;
import cn.edu.szu.myrpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        // 进行RPC
        User user = new User();
        user.setName("朱国荣");
        for (int i = 0; i < 3; i++) {
            User newUser = userService.getUser(user);
            if (newUser != null) {
                System.out.println(newUser.getName());
            } else {
                System.out.println("user == null");
            }
        }
    }
}

package cn.edu.szu.examplespringbootprovider;

import cn.edu.szu.example.common.model.User;
import cn.edu.szu.example.common.service.UserService;
import cn.edu.szu.myrpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("获取用户：" + user.getName());
        return user;
    }

}

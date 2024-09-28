package cn.edu.szu.example.provider;

import cn.edu.szu.example.common.model.User;
import cn.edu.szu.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("获取用户：" + user.getName());
        return user;
    }
}

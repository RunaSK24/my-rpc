package cn.edu.szu.example.common.service;

import cn.edu.szu.example.common.model.User;

public interface UserService {
    /**
     * 获取用户接口
     * @param user 用户
     * @return User
     */
    User getUser(User user);

    default double getNumber() {
        return 10.0;
    }
}

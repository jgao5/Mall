package com.jian.mall.service;

import com.jian.mall.common.ServerResponse;
import com.jian.mall.pojo.User;

import javax.servlet.http.HttpSession;

public interface IUserService {

    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str, String type);
    ServerResponse<String> passwordHint(String username);
    ServerResponse<String> hintAnswer(String username, String hint, String answer);
    ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);
    ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);
    ServerResponse<User> updateInfo(User user);
    ServerResponse<User> getInfo(Integer userId);
}

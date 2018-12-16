package com.jian.mall.service.impl;

import com.jian.mall.common.Const;
import com.jian.mall.common.ServerResponse;
import com.jian.mall.common.TokenCache;
import com.jian.mall.dao.UserMapper;
import com.jian.mall.pojo.User;
import com.jian.mall.service.IUserService;
import com.jian.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse response = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!response.isSuccess()) {
            return response;
        }
        response = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!response.isSuccess()) {
            return response;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    ServerResponse.createByErrorMessage("用户名已存在");
                }
            }

            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("检验成功");
    }

    @Override
    public ServerResponse<String> passwordHint(String username) {
        ServerResponse serverResponse = this.checkValid(username, Const.USERNAME);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String hint = userMapper.selectHintByUsername(username);
        if (StringUtils.isNotBlank(hint)) {
            return ServerResponse.createBySuccess(hint);
        }

        return ServerResponse.createByErrorMessage("密码提示问题为空");
    }

    @Override
    public ServerResponse<String> hintAnswer(String username, String hint, String answer) {
        int resultCount = userMapper.checkAnswer(username, hint, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey("token_" + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }
}

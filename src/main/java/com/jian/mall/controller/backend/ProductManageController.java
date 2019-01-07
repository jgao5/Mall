package com.jian.mall.controller.backend;

import com.jian.mall.common.Const;
import com.jian.mall.common.ResponseCode;
import com.jian.mall.common.ServerResponse;
import com.jian.mall.pojo.Product;
import com.jian.mall.pojo.User;
import com.jian.mall.service.IProductService;
import com.jian.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @RequestMapping("/save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession httpSession, Product product) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作");
        } else {
            return iProductService.saveOrUpdateProduct(product);
        }
    }

    @RequestMapping("/set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession httpSession, Integer productId, Integer status) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作");
        } else {
            return iProductService.setSaleStatus(productId, status);
        }
    }

    @RequestMapping("/get_detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession httpSession, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作");
        } else {
            //
            return null;
        }
    }

}

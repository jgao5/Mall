package com.jian.mall.controller.backend;

import com.jian.mall.common.Const;
import com.jian.mall.common.ResponseCode;
import com.jian.mall.common.ServerResponse;
import com.jian.mall.pojo.User;
import com.jian.mall.service.ICategoryService;
import com.jian.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession httpSession, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //check admin
        if (!iUserService.checkAdmin(user).isSuccess()){
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        } else {
            return iCategoryService.addCategory(categoryName, parentId);
        }
    }


    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession httpSession,Integer categoryId, String categoryName) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //check admin
        if (!iUserService.checkAdmin(user).isSuccess()){
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        } else {
            return iCategoryService.updateCategoryName(categoryName, categoryId);
        }
    }


    @RequestMapping(value = "get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildParallelCategory(HttpSession httpSession, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //check admin
        if (!iUserService.checkAdmin(user).isSuccess()){
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        } else {
            //查询集结点category信息，无递归，保持平级
            return iCategoryService.getChildParallelCategory(categoryId);
        }
    }


    @RequestMapping(value = "get_category_recursive.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryRecursive(HttpSession httpSession, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //check admin
        if (!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        } else {
            //查询当前节点Id 和递归子节点的Id
            return iCategoryService.getCategoryRecursive(categoryId);
        }
    }
}

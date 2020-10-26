package com.ex.oauthauthserver.controller;

import com.ex.oauthauthserver.Dao.ClientDaoImpl;
import com.ex.oauthauthserver.Dao.UserDaoImpl;
import com.ex.oauthauthserver.config.Md5PasswordEncoder;
import com.ex.oauthauthserver.model.OauthClientDetails;
import com.ex.oauthauthserver.model.User;
import com.ex.oauthauthserver.utils.JsonResult;
import com.ex.oauthauthserver.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private MongoTemplate mongoTemplate;
    @Autowired
    UserDaoImpl userDao;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object saveUser(@RequestBody User user) {
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        try {
            return userDao.saveUser(user);
        } catch (Exception e) {
            return "Fail";
        }
    }

    /**
     * 用户删除，uuid
     * @param userId
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String removeUser(@RequestParam("userId") String userId) {
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        try {
            userDao.removeUser("userId", userId);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    /*
     * 用户信息更新，必须包含Uuid
     * HttpServletRequest只能接受key/value类型数据，对应post内容就是form-data，无法接收JSON格式
     * @param
     * @return
     */
//    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
//    public Object updateUser(HttpServletRequest request) {
//        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
//        try {
//            return userDao.updateUser(request);
//        }catch (Exception e){
//            return "Fail";
//        }
//    }

    /**
     * 传入json字符串，然后进行更新。
     * @param userInfo
     * @return
     */
    //@RequestBody只支持json（JSON字符串,application/json）请求体
    // servlet只支持简单key/value型(json对象,form-data,application/x-www-form-urlencoded)
    // @RequestParam则两种都支持
    //requestBody就是请求中的request payload对应的就是JSON文件，需要使用Map来接收
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateUser(@RequestBody Map<String, Object> userInfo){
        try {
            return userDao.updateUserInfo(userInfo);
        }catch (Exception e){
            return "Fail";
        }
    }

    /**
     * 密码修改接口
     * @param email
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @RequestMapping(value = "/newPassword",method = RequestMethod.POST)
    public Object updateNewPassword(@RequestParam String email,@RequestParam String oldPwd,@RequestParam String newPwd) {
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        return userDao.updatePassword(email,oldPwd,newPwd);
    }

    /**
     * 忘记密码邮件发送
     * @param email
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public JsonResult getUser(@RequestParam String email){
        String result = userDao.resetPassword(email);
        return ResultUtils.success(result);
    }


    /**
     * 客户端注册，post json字符串
     * @param oauthClient
     * @return
     */
    @RequestMapping(value = "/addClient",produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public Object addClient(@RequestBody OauthClientDetails oauthClient){
        ClientDaoImpl clientDao = new ClientDaoImpl(mongoTemplate);
        String md5Secret = new Md5PasswordEncoder().encode(oauthClient.getClientSecret());
        oauthClient.setClientSecret(md5Secret);
        try {
            return clientDao.saveClient(oauthClient);
        }catch (Exception e){
            return "Fail";
        }
    }
    /*
    @RequestParam
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test(@RequestParam String name){
        return "ok";
    }

}

package com.ex.oauthauthserver.Dao;

import com.ex.oauthauthserver.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface IUserDao {

    int saveUser(User user);

    Object readUser(String key,String value);

    void removeUser(String key,String value);

    Object updateUser(HttpServletRequest request);

    Boolean isRegistered(String email);

    Object updateUserInfo(Map<String,Object> infoMap);
    String resetPassword(String email);
//    Boolean verifyPassword(String email,String password);

    int updatePassword(String email, String oldPwd,String newPwd);
}

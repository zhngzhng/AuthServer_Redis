package com.ex.oauthauthserver.Dao;

import com.ex.oauthauthserver.config.Md5PasswordEncoder;
import com.ex.oauthauthserver.model.User;
import com.ex.oauthauthserver.utils.Common;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Component
public class UserDaoImpl implements IUserDao{
    private final MongoTemplate mongoTemplate;
    @Autowired
    Common commonServer;

    @Autowired
    public UserDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int saveUser(User user) {
        Query query = Query.query(Criteria.where("email").is(user.getEmail()));
        if (!mongoTemplate.find(query, User.class).isEmpty()) {
            return -1;
        } else {
            String userId = UUID.randomUUID().toString();
            user.setUserId(userId);
            user.setPassword(new Md5PasswordEncoder().encode(user.getPassword()));
//            创建时间
            Long time = Long.parseLong(System.currentTimeMillis() + "");
            Date createDate = new Date(time);
            user.setCreatTime(createDate);
            mongoTemplate.save(user);
            return 1;
        }
    }

    @Override
    public void removeUser(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, "users");
    }

//    @Override
//    public Object updatePassword(String email, String password) {
//        try {
//            if (!isRegistered(email)) {
//                return "None";
//            } else {
//                Query query = new Query(Criteria.where("email").is(email));
//                Update updatePassword = new Update();
//                updatePassword.set("password", new Md5PasswordEncoder().encode(password));
//                mongoTemplate.updateFirst(query, updatePassword, User.class);
//                return mongoTemplate.findOne(query, User.class);
//            }
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }
    @Override
    public Object readUser(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, User.class).isEmpty()) {
            return "None";
        } else {
            User user = mongoTemplate.findOne(query, User.class);
            user.setPassword("");
            return user;
        }
    }
    @Override
    public Boolean isRegistered(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return !mongoTemplate.find(query, User.class).isEmpty();
    }

    @Override
    public Object updateUserInfo(Map<String, Object> infoMap) {
        String userId = (String) infoMap.get("userId");
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = commonServer.setUpdate(infoMap);
        try{
            mongoTemplate.updateFirst(query, update, User.class);
            return "Suc";
        }catch (Exception e){
            return "Fail";
        }
    }

    //重置密码邮件
    @Override
    public String resetPassword(String email) {
        try {
            Random random = new Random();
            String password = "";
            for (int i = 0; i < 8; i++) {
                int num = random.nextInt(62);
                if (num >= 0 && num < 10) {
                    password += num;
                } else if (num >= 10 && num < 36) {
                    num -= 10;
                    num += 65;
                    char c = (char) num;
                    password += c;
                } else {
                    num -= 36;
                    num += 97;
                    char c = (char) num;
                    password += c;
                }
            }
            User user = mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), User.class);
            if (user !=null){
                //重置密码，使用随机生成的密码替代原来的密码
//                user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
                String md5Pwd = new Md5PasswordEncoder().encode(password);
                Update update = new Update().set("password", md5Pwd);
                mongoTemplate.upsert(new Query(Criteria.where("email").is(email)), update,User.class);
//              完成密码更新，发送邮件
                String subject = "OpenGMS Portal Password Reset";
                String content = "Hello " + user.getName() + ":<br/>" +
                        "Your password has been reset to <b>" + password + "</b>. You can use it to change the password.<br/>" +
                        "Welcome to <a href='https://geomodeling.njnu.edu.cn' target='_blank'>OpenGMS</a> !";

                //邮件开始发送
                Boolean flag = commonServer.sendEmail(email, subject, content);
                if (flag){
                    return "suc";
                }else {
                    return "send fail";
                }

            }else {
                return "no user";
            }
        }catch (Exception e){
            return "error";
        }
    }

//  密码修改
    @Override
    public int updatePassword(String email, String oldPwd, String newPwd) {
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class);
        String md5Pwd = new Md5PasswordEncoder().encode(oldPwd);
        if (user.getPassword().equals(md5Pwd)){
            try {
                mongoTemplate.updateFirst(query,new Update().set("password",new Md5PasswordEncoder().encode(newPwd)), User.class);
                return 1;
            }catch (Exception e){
                return -1;
            }
        }else {
            return 0;
        }
    }
}

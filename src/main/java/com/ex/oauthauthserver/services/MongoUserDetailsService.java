package com.ex.oauthauthserver.services;

import com.ex.oauthauthserver.Dao.UserDaoImpl;
import com.ex.oauthauthserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("mongoUserDetailsService")
public class MongoUserDetailsService implements UserDetailsService {
//    集合名
    private final String USER_COLLECTION = "users";
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), User.class, USER_COLLECTION);
        if(user !=null){
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
            return buildUserForAuthentication(user, authorities);
        }else {
            throw new UsernameNotFoundException("Email not found!");
        }
    }
//  UserDetails转换
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities){
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }
}

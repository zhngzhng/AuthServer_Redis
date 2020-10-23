package com.ex.oauthauthserver;

import com.ex.oauthauthserver.config.Md5PasswordEncoder;
import com.ex.oauthauthserver.model.OauthClientDetails;
import com.ex.oauthauthserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.UUID;

@SpringBootApplication
public class OauthauthserverApplication extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(OauthauthserverApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(OauthauthserverApplication.class);
    }
    //        @Autowired
//    MongoTemplate mongoTemplate;
////    初始化客户端
//    @Bean
//    CommandLineRunner initClient(){
//        return args -> {
//            OauthClientDetails client =  mongoTemplate.findOne(new Query(Criteria.where("clientId").is("portal")), OauthClientDetails.class,"oauth2ClientsDetails");
//            if(client == null){
//                OauthClientDetails newClient = new OauthClientDetails();
//                newClient.setClientId("portal");
//                newClient.setClientSecret(new Md5PasswordEncoder().encode("xukai"));
//                newClient.setAuthorizedGrantTypes("password, authorization_code,refresh_token");
//                newClient.setScope("all");
//                newClient.setRegisteredRedirectUris("http://example.com");
//                newClient.setResourceIds("resource");
//                mongoTemplate.save(newClient);
//            }
//        };
//    }
////    初始化用户
//    @Bean
//    CommandLineRunner initUser(){
//        return args -> {
//            User user = mongoTemplate.findOne(new Query(Criteria.where("email").is("zhengzhong@hotmail.com")),User.class, "users");
//            if(user == null){
//                User newUser = new User();
//                String userId = UUID.randomUUID().toString();
//                newUser.setUserId(userId);
//                newUser.setEmail("zhengzhong@hotmail.com");
//                newUser.setPassword(new Md5PasswordEncoder().encode("123456"));
//                mongoTemplate.save(newUser);
//            }
//
//        };
//    }
}

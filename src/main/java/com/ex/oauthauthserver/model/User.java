package com.ex.oauthauthserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    /**
     *
     */
    //    _id(fixed):mongodb自动生成
    private String userId; //uuid
    @Email
    @NotNull(message = "{user.email.notnull}")
    private String email;
    private String password;  //MD5 在后端进行MD5编码
    @NotNull(message = "{user.name.notNull}")
    private String name;

    @Size(min = 8,max = 15, message = "{user.phoneNum.size}")
    private long phoneNum;
    private UserTitle title;
    private String country;
    private String state;
    private String city;
    private String homePage;
    //动态数组，只需要对应String[]即可
    private ArrayList<String> organizations;
    private String introduction;
    //用户对象
    private ArrayList<Object> resources;

    //用户无法自行修改部分
    private Date creatTime;
    private ArrayList<String> loginIp;
    private ArrayList<String> domain;
}

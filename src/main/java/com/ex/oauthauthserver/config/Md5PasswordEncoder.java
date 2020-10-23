package com.ex.oauthauthserver.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null){
            throw new IllegalArgumentException("password can not be null!");
        }
        else {
            return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodePassword) {
        if (rawPassword == null){
            throw new IllegalArgumentException("password can not be null!");
        }
        else {
            if(DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()).equals(encodePassword)){
                return true;
            }
            else {
                return false;
            }
        }
    }
}

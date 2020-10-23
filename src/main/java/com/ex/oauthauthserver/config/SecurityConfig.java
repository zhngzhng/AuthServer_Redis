package com.ex.oauthauthserver.config;

import com.ex.oauthauthserver.services.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
// PasswordEncoder，配置后密码验证通过此步骤
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new Md5PasswordEncoder();
    }
    @Bean
    public UserDetailsService mongoUserDetailsSer(){
        return new MongoUserDetailsService();
    }

//  用户认证
    @Override
    protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception{
        //需要的是UserDetailsService
        authBuilder.userDetailsService(mongoUserDetailsSer());

    }
//    密码授权模式需要这个AuthenticationManager的Bean
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

//    配置静态资源，全局忽略
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/img/**");
        web.ignoring().antMatchers("/element-ui/**");
        web.ignoring().antMatchers("/js/**");
    }
    @Autowired
    AddLoginIpAddrFilter addLoginIpAddrFilter;
    //忽略已经校验过的用户
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(addLoginIpAddrFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/resetPwd").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").loginProcessingUrl("/in").failureUrl("/login?error=true")
//               配置验证端口
                .usernameParameter("account").passwordParameter("password")
                .and()
                //设置成basic登录，前端可以使用application/x-www-form-urlencoded
                .httpBasic().disable();
    }
}

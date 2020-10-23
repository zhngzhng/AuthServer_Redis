package com.ex.oauthauthserver.config;

import com.ex.oauthauthserver.services.MongoClientDetailsService;
import com.ex.oauthauthserver.services.MongoUserDetailsService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthServer extends AuthorizationServerConfigurerAdapter {
    private final @NonNull AuthenticationManager authenticationManager;
    private final @NonNull RedisConnectionFactory redisConnectionFactory;
    public OAuth2AuthServer(@NonNull AuthenticationManager authenticationManager, @NonNull RedisConnectionFactory redisConnectionFactory) {
        this.authenticationManager = authenticationManager;
        this.redisConnectionFactory = redisConnectionFactory;
    }
//    数据源
//    @Autowired
//    private ClientDetailsService mongoClientDetailsService;
    @Autowired
    private MongoUserDetailsService mongoUserDetailsService;
    /*
    基于mongoDB存储，读取client信息，以下两个步骤
    1.构建clientDetailsServer，通过它来链接数据库
    2.将ClientDetailsServer配置进ClientDetailsServiceConfigurer中
     */
    @Bean
    public ClientDetailsService clientDetails(){
        return new MongoClientDetailsService();
    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
        clients.withClientDetails(clientDetails());
    }

    /*
    AuthorizationServerEndpointsConfigurer配置
    非安全管理，如令牌存储等内容
    配置redis存储令牌
     */
    public TokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception{
        endpoints.authenticationManager(this.authenticationManager)
                .tokenStore(redisTokenStore());
        endpoints.userDetailsService(mongoUserDetailsService);
    }

    /*
    开放check_token端口，提供给资源服务器验证并解析token获取用户信息
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer securityConfigurer) throws Exception{
        securityConfigurer.checkTokenAccess("isAuthenticated()");
    }
}

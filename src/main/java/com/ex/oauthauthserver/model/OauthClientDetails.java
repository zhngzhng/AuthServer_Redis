package com.ex.oauthauthserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "oauth2ClientsDetails")
public class OauthClientDetails {
    private String clientId;
    private String clientSecret;
    private String resourceIds;
    private String scope;
    private String authorizedGrantTypes;
    private String registeredRedirectUris;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private String additionalInformation;
    private String authorities;
//  设置用户是否自动Approval操作，默认为false,包括true,false,read,write
//  该字段只适用于code模式，当用户登录成功后，若值为‘true’或支持的值，则会跳过Approve界面
    private String autoapprove;
    private String autoApproveScopes;
}


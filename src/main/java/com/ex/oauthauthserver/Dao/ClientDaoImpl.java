package com.ex.oauthauthserver.Dao;

import com.ex.oauthauthserver.model.OauthClientDetails;
import org.springframework.data.mongodb.core.MongoTemplate;

public class ClientDaoImpl implements IClientDao{
    private final MongoTemplate mongoTemplate;

    public ClientDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Object saveClient(OauthClientDetails oauthClient) {
        try {
            mongoTemplate.save(oauthClient);
            return oauthClient.getClientId();
        }catch (Exception e){
            return "Fail";
        }
    }
}

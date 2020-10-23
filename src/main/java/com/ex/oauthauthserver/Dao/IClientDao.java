package com.ex.oauthauthserver.Dao;

import com.ex.oauthauthserver.model.OauthClientDetails;

public interface IClientDao {
    Object saveClient(OauthClientDetails oauthClient);
}

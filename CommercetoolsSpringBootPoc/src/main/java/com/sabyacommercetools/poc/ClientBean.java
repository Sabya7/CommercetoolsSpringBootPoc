package com.sabyacommercetools.poc;


import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.defaultconfig.ApiFactory;
import com.commercetools.api.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:ctp.properties")
public class ClientBean {

    @Autowired
    private Environment env;

    @Bean(name = "client")
    public ApiRoot createClient()
    {

        ApiRoot apiRoot = ApiFactory.create(
                ClientCredentials.of()
                        .withClientId(env.getProperty("ctp.clientId"))
                        .withClientSecret(env.getProperty("ctp.clientSecret"))
                        .withScopes(env.getProperty("ctp.scopes"))
                        .build(),
                ServiceRegion.GCP_AUSTRALIA_SOUTHEAST1.getOAuthTokenUrl(),
                ServiceRegion.GCP_AUSTRALIA_SOUTHEAST1.getApiUrl()
        );

        return apiRoot;
    }

}

package com.sabyacommercetools.officialtraining;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.defaultconfig.ApiFactory;
import com.commercetools.api.defaultconfig.ServiceRegion;
import com.commercetools.importapi.defaultconfig.ImportApiFactory;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:ctp.training.properties")
public class ClientBeanTraining {

    @Autowired
    private Environment env;

    @Bean(name = "clientTraining")
    public ApiRoot createClient()
    {

        ApiRoot apiRoot = ApiFactory.create(
                ClientCredentials.of()
                        .withClientId(env.getProperty("ctp.clientId.t"))
                        .withClientSecret(env.getProperty("ctp.clientSecret.t"))
                        .withScopes(env.getProperty("ctp.scopes.t"))
                        .build(),
                ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(),
                ServiceRegion.GCP_EUROPE_WEST1.getApiUrl()
        );

        return apiRoot;
    }
    @Bean(name = "clientImpTraining")
    public com.commercetools.importapi.client.ApiRoot createImportClient()
    {

        com.commercetools.importapi.client.ApiRoot apiRoot = ImportApiFactory.create(
                ClientCredentials.of()
                        .withClientId(env.getProperty("ctp.clientId.t"))
                        .withClientSecret(env.getProperty("ctp.clientSecret.t"))
                        .withScopes(env.getProperty("ctp.scopes.t"))
                        .build(),
                com.commercetools.importapi.defaultconfig.ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(),
                com.commercetools.importapi.defaultconfig.ServiceRegion.GCP_EUROPE_WEST1.getApiUrl()
        );

        return apiRoot;
    }


}

package com.sabyacommercetools.marut.configuration;

import com.commercetools.importapi.client.ApiRoot;
import com.commercetools.importapi.models.importsinks.ImportSink;
import com.commercetools.importapi.models.importsinks.ImportSinkDraftBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutionException;

@Configuration
public class ImportApiUtils {

    //create importAPIClient here, for the time being we are autowiring it.
    @Autowired
    ApiRoot clientImpTraining;

    @Value("${ct.project}")
    private String project;

    @Bean(name = "importSink")
    public ImportSink getImportSink() throws ExecutionException, InterruptedException {
        return
                clientImpTraining.withProjectKeyValue(project)
                .importSinks()
                        .withImportSinkKeyValue("vendor-sink")
                        .get()
                .execute().get().getBody();
    }

}

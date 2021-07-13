package com.sabyacommercetools.poc;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.api_client.ApiClientPagedQueryResponse;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/")
public class Demo {

    @Autowired
    private ApiRoot client;

    @GetMapping("/client")
    public ApiHttpResponse<ApiClientPagedQueryResponse> getCli() throws ExecutionException, InterruptedException {
//        System.out.println("sabya");

//        String s = "hero";
        CompletableFuture<ApiHttpResponse<ApiClientPagedQueryResponse>> s =  client
                .withProjectKey("demo-project")
                .apiClients()
                .get()
                .execute();
//                .getBodyAsString();

//        client.withProjectKey("demo-project");
//       int i=0;
//       while(!s.isDone())
//       {
//           Thread.sleep(500);
//           System.out.println(i++);
//       }


//        System.out.println(s.get());
        return s.get();
    }
}

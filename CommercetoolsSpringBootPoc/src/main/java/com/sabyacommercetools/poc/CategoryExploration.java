package com.sabyacommercetools.poc;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.category.Category;
import com.commercetools.api.models.category.CategoryPagedQueryResponse;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class CategoryExploration {

    @Autowired
    private ApiRoot client;


    @GetMapping("/getCat1")
    public ApiHttpResponse<Category> getCatbyId() throws ExecutionException, InterruptedException {
        CompletableFuture<ApiHttpResponse<Category>> s =  client
                .withProjectKey("demo-project")
                .categories()
                .withId("4fe802eb-7991-43e8-bf27-73497136556a")
                .get()
                .execute();

        return s.get();
    }

    @GetMapping("/getCats")
    public ApiHttpResponse<CategoryPagedQueryResponse> getCats() throws ExecutionException, InterruptedException {

        CompletableFuture<ApiHttpResponse<CategoryPagedQueryResponse>> z =  client
                .withProjectKey("demo-project")
                .categories()
                .get()
                .execute();

        return z.get();
    }
    @GetMapping("/getCatw")
    public ApiHttpResponse<CategoryPagedQueryResponse> getCatw() throws ExecutionException, InterruptedException {
        CompletableFuture<ApiHttpResponse<CategoryPagedQueryResponse>> s =  client
                .withProjectKey("demo-project")
                .categories()
                .get()
                .addWhere("ancestors is empty")
                .execute();

        return s.get();
    }
}

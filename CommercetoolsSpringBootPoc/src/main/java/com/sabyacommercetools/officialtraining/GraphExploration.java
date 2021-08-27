package com.sabyacommercetools.officialtraining;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.graph_ql.GraphQLRequest;
import com.commercetools.api.models.graph_ql.GraphQLRequestBuilder;
import com.commercetools.api.models.graph_ql.GraphQLResponse;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class GraphExploration {

    @Autowired
    ApiRoot clientTraining;


    @RequestMapping("/grphQryTrng")
    public ApiHttpResponse<GraphQLResponse> graphQuery() throws ExecutionException, InterruptedException {
//        GraphQLResponseEntity responseEntity =
//                new GraphQLTemplate()
//                        .query(
//                                GraphQLRequestEntity.Builder()
//                                        .url(ServiceRegion.GCP_EUROPE_WEST1.getApiUrl() + "/" + projectKey + "/graphql")
//                                        .request(ProductData.class)
//                                        .arguments(new Arguments("products",
//                                                new Argument("limit", 2),
//                                                new Argument("sort", "id desc")
//                                        ))
//                                        .build(),
//                                ProductData.class
//                        );

        GraphQLRequest request = GraphQLRequestBuilder.of()
                .query("query{\n" +
                        "  products{\n" +
                        "    total\n" +
                        "    results{\n" +
                        "      id\n" +
                        "      key\n" +
                        "      productType{\n" +
                        "        name\n" +
                        "      }\n" +
                        "      masterData{\n" +
                        "        current{\n" +
                        "          name(locale:\"en\")\n" +
                        "          description(locale:\"en\")\n" +
                        "        }\n" +
                        "      }\n" +
                        "      skus\n" +
                        "    }\n" +
                        "  }\n" +
                        "}").build();

        CompletableFuture<ApiHttpResponse<GraphQLResponse>> response = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .graphql()
                .post(request)
                .execute();


        return response.get();
    }
}

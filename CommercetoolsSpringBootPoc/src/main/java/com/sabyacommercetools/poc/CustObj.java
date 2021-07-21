package com.sabyacommercetools.poc;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.custom_object.CustomObjectDraft;
import com.commercetools.api.models.custom_object.CustomObjectDraftBuilder;
import com.commercetools.api.models.customer.*;
import com.commercetools.api.models.type.*;
import com.commercetools.importapi.models.common.LocalizedStringBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class CustObj {

    @Autowired
    ApiRoot client;

    @GetMapping("/cust")
    public ApiHttpResponse<CustomObject> createCust() throws ExecutionException, InterruptedException {
        CustomObjectDraft c = CustomObjectDraftBuilder.of()
                .container("SabyaCustObj")
                .key("cust1")
                .value("12345")
                .version(0L)
                .build();

        CompletableFuture<ApiHttpResponse<CustomObject>> customObject =
                client.withProjectKey("demo-project")
                .customObjects()
                .post(c)
                .execute();

        return customObject.get();

    }
    @PostMapping("/creCustFld")
    public ApiHttpResponse<CustomerSignInResult> creCustomField(@RequestBody CustomerPojo pojo) throws ExecutionException, InterruptedException {

        LocalizedString name = LocalizedString.of();
        LocalizedString label = LocalizedString.of();
                name.setValue("en","sab_cust");
                label.setValue("en","cutomising customer");

         FieldType fieldType = CustomFieldStringTypeBuilder.of().build();

        FieldDefinition fieldDefinition =
                FieldDefinitionBuilder.of()
                .type(fieldType)
                .name("FavMovie")
                .label(label)
                .required(false)
                .build();

//
//        TypeDraft typeDraft = TypeDraftBuilder.of()
//                .key("sabya_custom1_customer")
//                .name(name)
//                .resourceTypeIds(ResourceTypeId.CUSTOMER)
//                .fieldDefinitions(fieldDefinition)
//                .build();
        //Should Be Created Once
//        ApiHttpResponse<Type> typeApiHttpResponse = client.withProjectKey("demo-project")
//                .types()
//                .post(typeDraft)
//                .executeBlocking();

//        System.out.println(typeApiHttpResponse);


        TypeResourceIdentifier resourceIdentifier = TypeResourceIdentifierBuilder.of()
                .key("sabya_custom1_customer")
                .build();

        FieldContainer fieldContainer = FieldContainerBuilder.of()
                .addValue("FavMovie",pojo.favMovie)
                .build();

        CustomFieldsDraft customFieldsDraft = CustomFieldsDraftBuilder.of()
                .type(resourceIdentifier)
                .fields(fieldContainer)
                .build();

        CustomerDraft customerDraft
                = CustomerDraftBuilder.of()
                .email(pojo.email)
                .firstName(pojo.firstName)
                .lastName(pojo.lastName)
                .custom(customFieldsDraft)
                .password("****2")
                .build();

        CompletableFuture<ApiHttpResponse<CustomerSignInResult>> customer =  client.withProjectKey("demo-project")
                .customers()
                .post(customerDraft)
                .execute();

        return customer.get();



    }
}

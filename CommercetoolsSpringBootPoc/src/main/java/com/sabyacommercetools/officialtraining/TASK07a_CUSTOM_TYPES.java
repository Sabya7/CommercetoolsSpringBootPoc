package com.sabyacommercetools.officialtraining;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.custom_object.CustomObjectDraft;
import com.commercetools.api.models.custom_object.CustomObjectDraftBuilder;
import com.commercetools.api.models.type.*;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class TASK07a_CUSTOM_TYPES {

    @Autowired
    ApiRoot clientTraining;

    @RequestMapping("/creCustTypeTrng")
    public void creCustType()
    {

        LocalizedString name = LocalizedString.of();
        LocalizedString label = LocalizedString.of();
        name.setValue("en","sab_cust");
        label.setValue("en","cutomising customer");

        FieldType fieldType = CustomFieldStringTypeBuilder.of().build();

        FieldDefinition fieldDefinition =
                FieldDefinitionBuilder.of()
                        .type(fieldType)
                        .name("Sab_Plant_Checker")
                        .label(label)
                        .required(false)
                        .build();

        TypeDraft typeDraft = TypeDraftBuilder.of()
                .key("sabya_custom_customer")
                .name(name)
                .resourceTypeIds(ResourceTypeId.CUSTOMER)
                .fieldDefinitions(fieldDefinition)
                .build();

        ApiHttpResponse<Type> typeApiHttpResponse = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .types()
                .post(typeDraft)
                .executeBlocking();

    }
    @RequestMapping("/creCustObjTrng")
    public ApiHttpResponse<CustomObject> creCustObject() throws ExecutionException, InterruptedException {
        CustomObjectDraft c = CustomObjectDraftBuilder.of()
                .container("SabyaCustObj")
                .key("Plant_Checker")
                .value("TULIPS")
                .version(0L)
                .build();

        CompletableFuture<ApiHttpResponse<CustomObject>> customObject =
                clientTraining.withProjectKey("training-0816-mixed-java-dev")
                        .customObjects()
                        .post(c)
                        .execute();

        return customObject.get();

    }
}

package com.louishoughton;

import io.quarkus.arc.profile.IfBuildProfile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class DynamoClientProducer {

        @Produces
        @IfBuildProfile("prod")
        public DynamoDbClient client() {
            return DynamoDbClient.builder()
                    .region(Region.EU_WEST_1)
                    .httpClient(software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient.builder().build())
                    .build();
        }
}

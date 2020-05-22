package com.louishoughton;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DynamoDbJsonToRequest {

    public CreateTableRequest fromJson(String file) throws IOException {
        InputStream contentIS = TokenUtils.class.getResourceAsStream(file);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        JsonNode jsonObject = new ObjectMapper().readValue(new String(tmp, 0, length, "UTF-8"), JsonNode.class);
        return CreateTableRequest.builder()
                .tableName(jsonObject.get("TableName").asText())
                .billingMode(jsonObject.get("BillingMode").asText())
                .attributeDefinitions(getAttributeDefinitions(jsonObject))
                .keySchema(getKeySchema(jsonObject))
                .globalSecondaryIndexes(getGsis(jsonObject))
                .build();
    }

    private Collection<GlobalSecondaryIndex> getGsis(JsonNode jsonObject) {
        JsonNode indexes = jsonObject.get("GlobalSecondaryIndexes");
        return StreamSupport.stream(indexes.spliterator(), false).map(index -> {
            return GlobalSecondaryIndex.builder()
                    .keySchema(getKeySchema(index))
                    .indexName(index.get("IndexName").asText())
                    .projection(Projection.builder().projectionType(index.get("Projection").get("ProjectionType").asText()).build())
                    .build();
        }).collect(Collectors.toList());
    }

    private Collection<KeySchemaElement> getKeySchema(JsonNode jsonObject) {
        JsonNode keySchema = jsonObject.get("KeySchema");
        return StreamSupport.stream(keySchema.spliterator(), false).map(key -> KeySchemaElement.builder()
                .keyType(key.get("KeyType").asText())
                .attributeName(key.get("AttributeName").asText())
                .build()).collect(Collectors.toList());
    }

    private List<AttributeDefinition> getAttributeDefinitions(JsonNode jsonObject) {
        JsonNode attributeDefinitions = jsonObject.get("AttributeDefinitions");
        return StreamSupport.stream(attributeDefinitions.spliterator(), false).map(json -> AttributeDefinition.builder()
                .attributeName(json.get("AttributeName").asText())
                .attributeType(json.get("AttributeType").asText())
                .build()).collect(Collectors.toList());
    }
}

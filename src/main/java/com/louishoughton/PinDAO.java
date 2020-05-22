package com.louishoughton;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class PinDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PinDAO.class);

    @Inject
    DynamoDbClient client;

    public Pin save(Pin pin) {
        String pk = "PIN_" + UUID.randomUUID();
        pin.setId(pk);
        String sk = pin.getUser();
        Map<String, AttributeValue> item = ImmutableMap.of(
                "PK", AttributeValue.builder().s(pk).build(),
                "SK", AttributeValue.builder().s(sk).build(),
                "Data", AttributeValue.builder().s(pin.getDescription()).build(),
                "Lat", AttributeValue.builder().n(Double.toString(pin.getLat())).build(),
                "Lon", AttributeValue.builder().n(Double.toString(pin.getLon())).build());
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("travel-map")
                .item(item)
                .build();

        client.putItem(putItemRequest);
        return pin;
    }

    public List<Pin> getAll(String user) {

        HashMap<String, Condition> keyConditions = new HashMap<>();

        keyConditions.put(
                "SK",
                Condition.builder()
                        .comparisonOperator(ComparisonOperator.EQ)
                        .attributeValueList(AttributeValue.builder()
                                .s(user).build()).build());

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName("travel-map")
                .indexName("data")
                .keyConditions(keyConditions)
                .returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
                .build();

        QueryResponse query = client.query(queryRequest);
        LOG.info(query.items().toString());
        return query.items().stream().map(Pin::from).collect(Collectors.toList());
    }



}

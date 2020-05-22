package com.louishoughton;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public class Pin {
    private String id;
    private String user;
    private String description;
    private double lat;
    private double lon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public static Pin from(Map<String, AttributeValue> attributes) {
        final Pin pin = new Pin();
        pin.setId(attributes.get("PK").s());
        pin.setUser(attributes.get("SK").s());
        pin.setDescription(attributes.get("Data").s());
        pin.setLat(Double.parseDouble(attributes.get("Lat").n()));
        pin.setLon(Double.parseDouble(attributes.get("Lon").n()));
        return pin;
    }
}

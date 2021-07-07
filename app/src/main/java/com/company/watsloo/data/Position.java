package com.company.watsloo.data;

public class Position {

    String description;
    Double latitude;
    Double longitude;

    public Position() {}

    public Position(String description, Double latitude, Double longitude) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}

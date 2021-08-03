package com.company.watsloo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Position {

    public String description;
    public Double latitude;
    public Double longitude;
    public boolean isAudited;
    public boolean isEasterEgg;
    public HashMap<String, String> images;
    public ArrayList<String> stories;

    public Position() {}

    public Position(String description, Double latitude, Double longitude, boolean isAudited, boolean isEasterEgg, HashMap<String, String> images, ArrayList<String> stories) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isAudited = isAudited;
        this.isEasterEgg = isEasterEgg;
        this.images = images;
        this.stories = stories;
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

    public boolean getIsAudited() {
        return isAudited;
    }

    public void setIsAudited(boolean isAudited) {
        this.isAudited = isAudited;
    }

    public boolean getIsEasterEgg() {
        return isEasterEgg;
    }

    public void setIsEasterEgg(boolean isEasterEgg) {
        this.isEasterEgg = isEasterEgg;
    }

    public HashMap<String, String> getImages() {
        return images;
    }

    public void setImages(HashMap<String, String> images) {
        this.images = images;
    }

    public ArrayList<String> getStories() {
        return stories;
    }

    public void setStories(ArrayList<String> stories) {
        this.stories = stories;
    }

}

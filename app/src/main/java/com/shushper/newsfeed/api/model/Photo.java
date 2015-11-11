package com.shushper.newsfeed.api.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Photo extends RealmObject {

    @PrimaryKey
    private int id;

    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

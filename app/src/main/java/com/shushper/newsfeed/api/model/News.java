package com.shushper.newsfeed.api.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class News extends RealmObject {

    @PrimaryKey
    private String objectId;

    private String imageUrl;

    private String title;
    private String content;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

package com.shushper.newsfeed.api.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class News extends RealmObject {

    @PrimaryKey
    private String objectId;

    private String imageUrl;

    private String title;
    private String content;

    private Date createdAt;

    private RealmList<Photo> photos;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public RealmList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(RealmList<Photo> photos) {
        this.photos = photos;
    }
}

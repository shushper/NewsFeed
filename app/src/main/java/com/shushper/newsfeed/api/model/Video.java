package com.shushper.newsfeed.api.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Video extends RealmObject {

    @PrimaryKey
    private int id;

    private String videoUrl;

    private String thumbUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}

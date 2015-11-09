package com.shushper.newsfeed.api;

import com.shushper.newsfeed.api.model.News;

import retrofit.Call;
import retrofit.http.GET;


public interface ApiInterface {

    String API_URL = "https://api.parse.com/1/";

    @GET("classes/News")
    Call<ApiResponse<News>> getNews();

}

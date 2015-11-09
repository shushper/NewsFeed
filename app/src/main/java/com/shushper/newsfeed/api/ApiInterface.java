package com.shushper.newsfeed.api;

import com.shushper.newsfeed.api.model.News;

import retrofit.http.GET;
import retrofit.http.Headers;


public interface ApiInterface {

    String API_URL = "https://api.parse.com/1/";

    @Headers({
            "X-Parse-Application-Id: zgYc2rHs0d40x63tmPIJ70TKLp33Wchz92cXhx3T",
            "X-Parse-REST-API-Key: izwO6ngy0cBsjV3m3x0f0Xk2WvjQRovUXuZnsSjc"
    })
    @GET("/classes/News")
    ApiResponse<News> getNews();

}

package com.shushper.newsfeed.api;

import java.util.List;


public class ApiResponse<T> {

    private List<T> results;

    public List<T> getResults() {
        return results;
    }
}

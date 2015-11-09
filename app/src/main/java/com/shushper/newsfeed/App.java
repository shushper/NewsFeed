package com.shushper.newsfeed;

import android.app.Application;

import com.shushper.newsfeed.api.ApiInterface;
import com.shushper.newsfeed.api.ApiServiceHelper;
import com.shushper.newsfeed.helpers.RealmManager;


public class App extends Application {

    private static ApiInterface sApiInterface;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmManager.getInstance().initialize(this);
        ApiServiceHelper.getInstance().initialize(this);

    }
}

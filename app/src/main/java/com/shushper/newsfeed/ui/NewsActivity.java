package com.shushper.newsfeed.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.ApiServiceHelper;
import com.shushper.newsfeed.api.request.NewsFeedRequest;
import com.shushper.newsfeed.ui.adapters.NewsAdapter;

import java.io.IOException;

public class NewsActivity extends AppCompatActivity implements ApiServiceHelper.ApiServiceHelperListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        NewsAdapter adapter = new NewsAdapter();


        RecyclerView newsRecycler = (RecyclerView) findViewById(R.id.news_recycler);
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(new LinearLayoutManager(this));
        newsRecycler.setAdapter(adapter);

        ApiServiceHelper.getInstance().sendRequest(new NewsFeedRequest());

    }

    @Override
    protected void onResume() {
        super.onResume();
        ApiServiceHelper.getInstance().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApiServiceHelper.getInstance().removeListener(this);
    }

    @Override
    public void onRequestSuccess(String requestName) {

    }

    @Override
    public void onRequestStatusChanges(String requestName, int statusCode) {

    }

    @Override
    public void onRequestFailed(String requestName, int failureCode) {

    }

    @Override
    public void onIOException(String requestName, IOException exception) {
        if (requestName.equals(NewsFeedRequest.REQUEST_NAME)) {

            Toast.makeText(this, getString(R.string.io_exception, exception.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }
}

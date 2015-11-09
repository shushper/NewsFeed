package com.shushper.newsfeed.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.ApiServiceHelper;
import com.shushper.newsfeed.api.model.News;
import com.shushper.newsfeed.api.request.NewsFeedRequest;
import com.shushper.newsfeed.ui.adapters.NewsAdapter;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.RetrofitError;

public class NewsActivity extends AppCompatActivity implements ApiServiceHelper.ApiServiceHelperListener {
    private static final String TAG = "NewsActivity";

    private NewsAdapter mAdapter;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mRealm = Realm.getDefaultInstance();
        mAdapter = new NewsAdapter();


        RecyclerView newsRecycler = (RecyclerView) findViewById(R.id.news_recycler);
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(new LinearLayoutManager(this));
        newsRecycler.setAdapter(mAdapter);

        ApiServiceHelper.getInstance().sendRequest(new NewsFeedRequest());

        queryNews();
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
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void queryNews() {
        RealmResults<News> news = mRealm.allObjects(News.class);
        mAdapter.setNews(news);
    }

    @Override
    public void onRequestSuccess(String requestName) {
        Log.i(TAG, "onRequestSuccess: ");
        if (NewsFeedRequest.REQUEST_NAME.equals(requestName)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestStatusChanges(String requestName, int statusCode) {

    }

    @Override
    public void onRequestFailed(String requestName, int failureCode) {

    }


    @Override
    public void onRetrofitError(String requestName, RetrofitError error) {
        if (requestName.equals(NewsFeedRequest.REQUEST_NAME)) {

            Toast.makeText(this, getString(R.string.io_exception, error.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }
}

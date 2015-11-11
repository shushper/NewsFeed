package com.shushper.newsfeed.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.model.News;
import com.shushper.newsfeed.helpers.TimeFormatter;
import com.shushper.newsfeed.ui.adapters.PhotoGalleryAdapter;
import com.shushper.newsfeed.ui.adapters.VideoGalleryAdapter;
import com.shushper.newsfeed.ui.adapters.decoration.VerticalSpaceDecoration;
import com.squareup.picasso.Picasso;

import io.realm.Realm;


public class ItemActivity extends AppCompatActivity implements PhotoGalleryAdapter.Listener, VideoGalleryAdapter.Listener {

    private static final String EXTRA_ITEM_ID = "item_id";


    public static void start(Context context, String itemId) {
        Intent starter = new Intent(context, ItemActivity.class);
        starter.putExtra(EXTRA_ITEM_ID, itemId);
        context.startActivity(starter);
    }

    //////////////////////////////////////////////////////////////////////////

    private TextView     mItemTitle;
    private TextView     mItemDate;
    private TextView     mItemContent;
    private ImageView    mItemImage;
    private RecyclerView mGalleryRecycler;

    private Realm mRealm;

    private String mItemId;
    private News mItem;

    private TimeFormatter mTimeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.item_screen_title);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRealm = Realm.getDefaultInstance();
        mTimeFormatter = new TimeFormatter(TimeFormatter.PATTERN_NEWS_FEED);

        mItemId = getIntent().getStringExtra(EXTRA_ITEM_ID);
        mItem = mRealm.where(News.class).equalTo("objectId", mItemId).findFirst();

        findViews();
        initVies();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void findViews() {
        mItemTitle = (TextView) findViewById(R.id.item_title);
        mItemDate = (TextView) findViewById(R.id.item_date);
        mItemContent = (TextView) findViewById(R.id.item_content);
        mItemImage = (ImageView) findViewById(R.id.item_image);
        mGalleryRecycler = (RecyclerView) findViewById(R.id.item_gallery);
    }

    private void initVies() {
        mItemTitle.setText(mItem.getTitle());
        mItemDate.setText(mTimeFormatter.format(mItem.getCreatedAt()));
        mItemContent.setText(mItem.getContent());

        mGalleryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGalleryRecycler.addItemDecoration(new VerticalSpaceDecoration(this));

        if (mItem.getPhotos().size() > 0) {
            PhotoGalleryAdapter adapter = new PhotoGalleryAdapter();
            adapter.setPhotos(mItem.getPhotos());
            adapter.setListener(this);
            mGalleryRecycler.setAdapter(adapter);
        } else if (mItem.getVideos().size() > 0) {
            VideoGalleryAdapter adapter = new VideoGalleryAdapter();
            adapter.setVideos(mItem.getVideos());
            adapter.setListener(this);
            mGalleryRecycler.setAdapter(adapter);
        } else {
            mGalleryRecycler.setVisibility(View.GONE);
        }

        Picasso.with(this).load(mItem.getImageUrl()).into(mItemImage);
    }

    @Override
    public void onPhotoClick(int photoId) {
        PhotoViewerActivity.start(this, mItemId, photoId);
    }

    @Override
    public void onVideoClick(String videoUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

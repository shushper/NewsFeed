package com.shushper.newsfeed.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.model.News;
import com.shushper.newsfeed.helpers.TimeFormatter;
import com.shushper.newsfeed.ui.adapters.PhotoGalleryAdapter;
import com.shushper.newsfeed.ui.adapters.decoration.VerticalSpaceDecoration;
import com.squareup.picasso.Picasso;

import io.realm.Realm;


public class ItemActivity extends AppCompatActivity implements PhotoGalleryAdapter.Listener {

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
        } else {

        }

        Picasso.with(this).load(mItem.getImageUrl()).into(mItemImage);
    }

    @Override
    public void onPhotoClick(int photoId) {
        PhotoViewerActivity.start(this, mItemId, photoId);
    }
}

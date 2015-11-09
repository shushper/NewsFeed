package com.shushper.newsfeed.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.model.News;
import com.shushper.newsfeed.helpers.TimeFormatter;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

/**
 * Created by shushper on 09.11.2015.
 */
public class ItemActivity extends AppCompatActivity {

    private static final String EXTRA_ITEM_ID = "item_id";

    private TextView  mItemTitle;
    private TextView  mItemDate;
    private TextView  mItemContent;
    private ImageView mItemImage;

    public static void start(Context context, String itemId) {
        Intent starter = new Intent(context, ItemActivity.class);
        starter.putExtra(EXTRA_ITEM_ID, itemId);
        context.startActivity(starter);
    }

    private Realm mRealm;

    private News mItem;

    private TimeFormatter mTimeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mRealm = Realm.getDefaultInstance();
        mTimeFormatter = new TimeFormatter(TimeFormatter.PATTERN_NEWS_FEED);

        String itemId = getIntent().getStringExtra(EXTRA_ITEM_ID);
        mItem = mRealm.where(News.class).equalTo("objectId", itemId).findFirst();

        findViews();
        initVies();


    }

    private void findViews() {
        mItemTitle = (TextView) findViewById(R.id.item_title);
        mItemDate = (TextView) findViewById(R.id.item_date);
        mItemContent = (TextView) findViewById(R.id.item_content);
        mItemImage = (ImageView) findViewById(R.id.item_image);
    }

    private void initVies() {
        mItemTitle.setText(mItem.getTitle());
        mItemDate.setText(mTimeFormatter.format(mItem.getCreatedAt()));
        mItemContent.setText(mItem.getContent());

        Picasso.with(this).load(mItem.getImageUrl()).into(mItemImage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

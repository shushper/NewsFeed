package com.shushper.newsfeed.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.model.News;
import com.shushper.newsfeed.api.model.Photo;
import com.shushper.newsfeed.ui.fragments.PhotoFragment;

import java.util.List;

import io.realm.Realm;

public class PhotoViewerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String EXTRA_ITEM_ID  = "item_id";
    private static final String EXTRA_PHOTO_ID = "photo_id";

    public static void start(Context context, String itemId, int photoId) {
        Intent starter = new Intent(context, PhotoViewerActivity.class);
        starter.putExtra(EXTRA_ITEM_ID, itemId);
        starter.putExtra(EXTRA_PHOTO_ID, photoId);
        context.startActivity(starter);
    }

    //////////////////////////////////////////////////////////////////////////
    private static final String TAG = "PhotoViewerActivity";

    private Realm mRealm;

    private List<Photo> mPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extras = getIntent().getExtras();

        String itemId = extras.getString(EXTRA_ITEM_ID);

        mRealm = Realm.getDefaultInstance();

        News item = mRealm.where(News.class).equalTo("objectId", itemId).findFirst();
        mPhotos = item.getPhotos();

        ViewPager pager = (ViewPager) findViewById(R.id.photo_pager);
        pager.addOnPageChangeListener(this);


        PhotoPagerAdapter adapter = new PhotoPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);


        if (savedInstanceState == null) {

            int initPosition = 0;
            for (int i = 0; i < mPhotos.size(); i++) {
                Photo photo = mPhotos.get(i);
                int photoId = extras.getInt(EXTRA_PHOTO_ID);
                if (photo.getId() == photoId) {
                    initPosition = i;
                    break;
                }
            }

            updateTitle(initPosition);
            pager.setCurrentItem(initPosition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void updateTitle(int position) {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(getString(R.string.photo_viewer_title, position + 1, mPhotos.size()));
    }

    private class PhotoPagerAdapter extends FragmentStatePagerAdapter {

        public PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoFragment.newInstance(mPhotos.get(position).getImageUrl());
        }

        @Override
        public int getCount() {
            return mPhotos.size();
        }
    }


}

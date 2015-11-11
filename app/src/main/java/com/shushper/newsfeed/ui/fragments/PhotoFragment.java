package com.shushper.newsfeed.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shushper.newsfeed.R;
import com.squareup.picasso.Picasso;

import io.realm.Realm;


public class PhotoFragment extends Fragment {

    private static final String KEY_PHOTO_URL = "PHOTO_URL";

    public static PhotoFragment newInstance(String photoUrl) {

        Bundle args = new Bundle();
        args.putString(KEY_PHOTO_URL, photoUrl);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Realm mRealm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView view = (ImageView) inflater.inflate(R.layout.fragment_photo, container, false);

        Picasso.with(getContext()).load(getArguments().getString(KEY_PHOTO_URL)).into(view);

        return view;
    }
}

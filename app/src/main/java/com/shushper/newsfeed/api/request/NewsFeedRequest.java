package com.shushper.newsfeed.api.request;

import android.content.Context;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.shushper.newsfeed.api.ApiResponse;
import com.shushper.newsfeed.api.ApiServiceHelper;
import com.shushper.newsfeed.api.model.News;

import java.util.List;

import io.realm.Realm;


public class NewsFeedRequest extends ApiRequest {

    public static final String REQUEST_NAME = "NewsFeedRequest";

    public NewsFeedRequest() {
    }

    @Override
    public String getName() {
        return REQUEST_NAME;
    }

    @Override
    protected void doExecute(ResultReceiver callback, Context context) {

        ApiResponse<News> response = ApiServiceHelper.getInstance().getsApiInterface().getNews();
        Log.d(REQUEST_NAME, "doExecute: succuss");

        List<News> news = response.getResults();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(news);
        realm.commitTransaction();
        realm.close();
        callback.send(RESULT_SUCCESS, null);
    }


    /* ***************************************
                 PARCELABLE PART
     *************************************** */

    protected NewsFeedRequest(Parcel in) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }

    public static final Creator<NewsFeedRequest> CREATOR = new Creator<NewsFeedRequest>() {
        @Override
        public NewsFeedRequest createFromParcel(Parcel in) {
            return new NewsFeedRequest(in);
        }

        @Override
        public NewsFeedRequest[] newArray(int size) {
            return new NewsFeedRequest[size];
        }
    };
}

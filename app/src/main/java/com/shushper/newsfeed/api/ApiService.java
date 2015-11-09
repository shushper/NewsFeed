package com.shushper.newsfeed.api;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shushper.newsfeed.api.request.ApiRequest;


public class ApiService extends Service {
    private static final String TAG = "ApiService";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ApiRequest request = intent.getParcelableExtra(ApiServiceHelper.EXTRA_API_REQUEST);
        ResultReceiver resultReceiver = intent.getParcelableExtra(ApiServiceHelper.EXTRA_RESULT_RECEIVER);

        new RequestExecutor(request, resultReceiver, startId).execute();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class RequestExecutor extends AsyncTask<Void, Void, Void> {

        private ApiRequest     mApiRequest;
        private ResultReceiver mResultReceiver;

        private int mStartId;

        public RequestExecutor(ApiRequest apiRequest, ResultReceiver resultReceiver, int startId) {
            Log.d(TAG, "RequestExecutor: " + apiRequest.getName());
            mApiRequest = apiRequest;
            mResultReceiver = resultReceiver;
            mStartId = startId;
        }


        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "RequestExecutor: " + mApiRequest.getName() + " doInBackground");
            mApiRequest.execute(mResultReceiver, ApiService.this);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "RequestExecutor: " + mApiRequest.getName() + " onPostExecute");
            stopSelf(mStartId);
        }
    }
}

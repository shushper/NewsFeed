package com.shushper.newsfeed.api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shushper.newsfeed.api.request.ApiRequest;
import com.shushper.newsfeed.helpers.GsonUtcDateAdapter;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmObject;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;


public class ApiServiceHelper {
    private static final String TAG = "ApiServiceHelper";

    public static final String EXTRA_RESULT_RECEIVER = "result_callback";
    public static final String EXTRA_API_REQUEST     = "api_request";

    private static ApiServiceHelper sInstance = new ApiServiceHelper();
    private static ApiInterface sApiInterface;


    /**
     * Запросы, которые сейчас выполняеются
     */
    private ArrayList<String> mPendingRequests = new ArrayList<>();

    /**
     * Слушатели состояния выполнения запросов
     */
    private ArrayList<ApiServiceHelperListener> mListeners = new ArrayList<>();


    private Context mContext;


    public ApiServiceHelper() {
    }

    public void initialize(Context context) {
        mContext = context;
        sApiInterface = configureApiInterface();
    }

    public static ApiServiceHelper getInstance() {
        return sInstance;
    }

    public ApiInterface getsApiInterface() {
        return sApiInterface;
    }

    private ApiInterface configureApiInterface() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(Date.class, new GsonUtcDateAdapter())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiInterface.API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(ApiInterface.class);
    }



    public void sendRequest(ApiRequest request) {

        Log.i(TAG, "Send Request " + request.getName());

        if (isPending(request.getName())) {
            Log.d(TAG, "Request " + request.getName() + " is pending");
            return;
        }

        mPendingRequests.add(request.getName());

        ResultReceiver callback = new ApiResultReceiver(new Handler(), request.getName());

        Intent intent = new Intent(mContext, ApiService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, callback);
        intent.putExtra(EXTRA_API_REQUEST, request);

        mContext.startService(intent);
    }

    public boolean isPending(String requestName) {
        Log.i(TAG, "isPending: " + requestName);
        return mPendingRequests.contains(requestName);
    }


    private class ApiResultReceiver extends ResultReceiver {

        private String mRequestName;

        public ApiResultReceiver(Handler handler, String requestName) {
            super(handler);
            mRequestName = requestName;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            switch (resultCode) {
                case ApiRequest.RESULT_RETROFIT_ERROR:
                    mPendingRequests.remove(mRequestName);
                    RetrofitError error = (RetrofitError) resultData.getSerializable(ApiRequest.EXTRA_RETROFIT_ERROR);
                    notifyListenersAboutRetrofitError(error);
                    break;

                case ApiRequest.RESULT_SUCCESS:
                    mPendingRequests.remove(mRequestName);
                    notifyListenersAboutSuccess();
                    break;

                case ApiRequest.RESULT_STATUS:
                    int statusCode = resultData.getInt(ApiRequest.EXTRA_STATUS_CODE);
                    notifyListenersAboutStatusChanged(statusCode);
                    break;

                case ApiRequest.RESULT_FAILURE:
                    mPendingRequests.remove(mRequestName);
                    int failureCode = resultData.getInt(ApiRequest.EXTRA_FAILURE_CODE);
                    notifyListenersAboutFailure(failureCode);
                    break;
            }
        }

        private void notifyListenersAboutRetrofitError(RetrofitError error) {
            for (ApiServiceHelperListener listener : mListeners) {
                if (listener != null) {
                    listener.onRetrofitError(mRequestName, error);
                }
            }
        }

        private void notifyListenersAboutSuccess() {
            for (ApiServiceHelperListener listener : mListeners) {
                if (listener != null) {
                    listener.onRequestSuccess(mRequestName);
                }
            }
        }

        private void notifyListenersAboutFailure(int failureCode) {
            for (ApiServiceHelperListener listener : mListeners) {
                if (listener != null) {
                    listener.onRequestFailed(mRequestName, failureCode);
                }
            }
        }

        private void notifyListenersAboutStatusChanged(int statusCode) {
            for (ApiServiceHelperListener listener : mListeners) {
                if (listener != null) {
                    listener.onRequestStatusChanges(mRequestName, statusCode);
                }
            }
        }
    }


    public interface ApiServiceHelperListener {
        void onRequestSuccess(String requestName);

        void onRequestStatusChanges(String requestName, int statusCode);

        void onRequestFailed(String requestName, int failureCode);

        void onRetrofitError(String requestName, RetrofitError error);
    }

    public void addListener(ApiServiceHelperListener l) {
        mListeners.add(l);
    }

    public void removeListener(ApiServiceHelperListener l) {
        mListeners.remove(l);
    }


}

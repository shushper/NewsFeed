package com.shushper.newsfeed.api.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.WorkerThread;
import android.util.Log;

import retrofit.RetrofitError;


public abstract class ApiRequest implements Parcelable {
    private static final String TAG = "ApiRequest";

    /**
     * Запрос выполнен успешно
     */
    public static final int RESULT_SUCCESS = 0;

    /**
     * Запрос выполнен не успешно (
     */
    public static final int RESULT_FAILURE = 1;

    /**
     * Изменение статуса выполнения запроса.
     */
    public static final int RESULT_STATUS = 2;

    /**
     * Ошибка Retrofit
     */
    public static final int RESULT_RETROFIT_ERROR = 3;


    public static final String EXTRA_STATUS_CODE    = "status_code";
    public static final String EXTRA_FAILURE_CODE   = "failure_code";
    public static final String EXTRA_RETROFIT_ERROR = "retrofit_error";


    public abstract String getName();

    @WorkerThread
    public final void execute(ResultReceiver callback, Context context) {
        try {
            doExecute(callback, context);
        } catch (RetrofitError e) {
            Log.e(TAG, "execute: Retrofit error", e);
            callback.send(RESULT_RETROFIT_ERROR, createRetrofitErrorBundle(e));
        }
    }

    protected abstract void doExecute(ResultReceiver callback, Context context);

    private Bundle createRetrofitErrorBundle(RetrofitError error) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RETROFIT_ERROR, error);
        return bundle;
    }
}

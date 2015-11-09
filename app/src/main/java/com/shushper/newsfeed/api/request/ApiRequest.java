package com.shushper.newsfeed.api.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.WorkerThread;

import java.io.IOException;


public abstract class ApiRequest implements Parcelable {
    /**
     * Запрос выполнен успешно
     */
    public static final int RESULT_SUCCESS = 0;

    /**
     * Запрос выполнен не успешно (
     */
    public static final int RESULT_FAILURE = 1;

    /**
     * Ошибка IOExeption
     */
    public static final int RESULT_IO_EXCEPTION = 2;

    /**
     * Изменение статуса выполнения запроса.
     */
    public static final int RESULT_STATUS = 3;


    public static final String EXTRA_IO_EXCEPTION = "io_exception";
    public static final String EXTRA_STATUS_CODE  = "status_code";
    public static final String EXTRA_FAILURE_CODE = "failure_code";


    public abstract String getName();

    @WorkerThread
    public final void execute(ResultReceiver callback, Context context) {
        try {
            doExecute(callback, context);
        } catch (IOException e) {
            callback.send(RESULT_IO_EXCEPTION, createIOExceptionBundle(e));
        }
    }

    protected abstract void doExecute(ResultReceiver callback, Context context) throws IOException;

    private Bundle createIOExceptionBundle(IOException exception) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_IO_EXCEPTION, exception);
        return bundle;
    }
}

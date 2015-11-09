package com.shushper.newsfeed.api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.shushper.newsfeed.api.request.ApiRequest;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


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
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new HeadersInterceptor());

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(loggingInterceptor);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ApiInterface.class);
    }

    private class HeadersInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request request = original.newBuilder()
                                      .header("X-Parse-Application-Id", "zgYc2rHs0d40x63tmPIJ70TKLp33Wchz92cXhx3T")
                                      .header("X-Parse-REST-API-Key", "izwO6ngy0cBsjV3m3x0f0Xk2WvjQRovUXuZnsSjc")
                                      .method(original.method(), original.body())
                                      .build();

            return chain.proceed(request);
        }
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
                case ApiRequest.RESULT_IO_EXCEPTION:
                    mPendingRequests.remove(mRequestName);
                    IOException exception = (IOException) resultData.getSerializable(ApiRequest.EXTRA_IO_EXCEPTION);
                    notifyListenersAboutIOException(exception);
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

        private void notifyListenersAboutIOException(IOException exception) {
            for (ApiServiceHelperListener listener : mListeners) {
                if (listener != null) {
                    listener.onIOException(mRequestName, exception);
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

        void onIOException(String requestName, IOException exception);
    }

    public void addListener(ApiServiceHelperListener l) {
        mListeners.add(l);
    }

    public void removeListener(ApiServiceHelperListener l) {
        mListeners.remove(l);
    }


}

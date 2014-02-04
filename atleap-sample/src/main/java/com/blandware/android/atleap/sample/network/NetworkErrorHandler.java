package com.blandware.android.atleap.sample.network;

import android.util.Log;

import com.octo.android.robospice.exception.NoNetworkException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by agrebnev on 03.02.14.
 */
public class NetworkErrorHandler implements ErrorHandler {


    private static final String TAG = NetworkErrorHandler.class.getSimpleName();

    @Override
    public Throwable handleError(RetrofitError retrofitError) {
        if (retrofitError.isNetworkError()) {
            Log.w(TAG, "Cannot connect to " + retrofitError.getUrl());
            return new NoNetworkException();
        } else {
            Response response = retrofitError.getResponse();
            if (response != null && response.getStatus() == 401) {
                //throw our own exception about unauthorized access
                Log.w(TAG, "Access in not authorized " + retrofitError.getUrl());
            } else {
                Log.w(TAG, "Error while accessing " + retrofitError.getUrl());
            }
        }

        return retrofitError;
    }
}

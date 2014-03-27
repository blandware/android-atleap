package com.blandware.android.atleap.sample.network.retrofit;

import android.content.Context;
import android.util.Log;

import com.blandware.android.atleap.AppContext;
import com.blandware.android.atleap.auth.AuthHelper;
import com.blandware.android.atleap.sample.Constants;
import com.blandware.android.atleap.sample.exception.DeveloperErrorException;
import com.blandware.android.atleap.sample.exception.ServerErrorException;
import com.blandware.android.atleap.sample.exception.UnauthorizedException;
import com.octo.android.robospice.exception.NoNetworkException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

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
        }


        Response response = retrofitError.getResponse();
        if (response != null) {
            int status = response.getStatus();
            if (status == 401) {
                //throw our own exception about unauthorized access
                Log.w(TAG, "Access in not authorized " + retrofitError.getUrl());
                Context context = AppContext.getContext();
                AuthHelper.reCreateAuthTokenForLastAccountBlocking(context, Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, null);
                return new UnauthorizedException("Access in not authorized " + retrofitError.getUrl(), retrofitError);
            } else if (status >= 300) {
                Log.w(TAG, "Error " + String.valueOf(status) + " while accessing " + retrofitError.getUrl());
                return retrofitError;
            }
        }

        int index = ExceptionUtils.indexOfType(retrofitError, ServerErrorException.class);

        if (index >= 0) {
            List<Throwable> errorList = ExceptionUtils.getThrowableList(retrofitError);
            ServerErrorException serverErrorException = (ServerErrorException)errorList.get(index);
            if (serverErrorException instanceof DeveloperErrorException) {
                Log.e(TAG, "Developer error with code" + serverErrorException.getErrorCode(), serverErrorException);
            }
            return serverErrorException;
        }

        return retrofitError;
    }
}

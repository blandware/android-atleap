package com.blandware.android.atleap.sample.network.retrofit;

import android.content.Context;
import android.text.TextUtils;

import com.blandware.android.atleap.AppContext;
import com.blandware.android.atleap.auth.AuthHelper;
import com.blandware.android.atleap.sample.Constants;

import retrofit.RequestInterceptor;

/**
 * Created by agrebnev on 26.03.14.
 */
public class AuthRequestInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade requestFacade) {
        Context context = AppContext.getContext();
        String authToken = AuthHelper.getAuthTokenOfLastAccount(context, Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, null);
        if (!TextUtils.isEmpty(authToken))
            requestFacade.addHeader("Authorization", "token " + authToken);
    }
}
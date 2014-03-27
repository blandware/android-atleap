package com.blandware.android.atleap.sample.network.robospice;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by agrebnev on 26.03.14.
 */
public abstract class BaseRequest<T, R> extends RetrofitSpiceRequest<T, R> {
    public BaseRequest(Class<T> clazz, Class<R> retrofitedInterfaceClass) {
        super(clazz, retrofitedInterfaceClass);
        this.setRetryPolicy(new NetworkRetryPolicy());
    }
}
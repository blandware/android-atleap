package com.blandware.android.atleap.sample.network.retrofit;

import com.blandware.android.atleap.sample.BuildConfig;
import com.blandware.android.atleap.sample.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * Created by agrebnev on 26.03.14.
 */
public class RetrofitHelper {

    public static RestAdapter.Builder createApiGithubRestAdapter(RestAdapter.Builder builder) {
        return createApiGithubNoAuthRestAdapter(builder)
                .setErrorHandler(new NetworkErrorHandler())
                .setRequestInterceptor(new AuthRequestInterceptor());
    }

    public static RestAdapter.Builder createApiGithubNoAuthRestAdapter(RestAdapter.Builder builder) {
        return createBaseRestAdapter(builder)
                .setEndpoint(Constants.API_GITHUB_BASE_URL);
    }


    public static RestAdapter.Builder createGithubRestAdapter(RestAdapter.Builder builder) {
        return createBaseRestAdapter(builder).setEndpoint(Constants.GITHUB_BASE_URL);
    }

    public static RestAdapter.Builder createBaseRestAdapter(RestAdapter.Builder builder) {
        if (builder == null)
            builder = new RestAdapter.Builder();

        builder.setConverter(new JacksonConverter(new ObjectMapper()));

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        } else {
            builder.setLogLevel(RestAdapter.LogLevel.NONE);
        }

        return builder;
    }
}

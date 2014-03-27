package com.blandware.android.atleap.sample.model;

import android.text.TextUtils;

import com.blandware.android.atleap.sample.exception.ServerErrorException;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by agrebnev on 26.03.14.
 */
public class GetAccessTokenResult {

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("scope")
    public String scope;

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("error_description")
    public String errorDescription;

    @JsonProperty("error_uri")
    public String errorUri;

    @JsonProperty("error")
    public String error;

    public String getError() {
        return error;
    }

    public void setError(String error) throws ServerErrorException {
        this.error = error;

        if (!TextUtils.isEmpty(error)) {
            throw new ServerErrorException(error, errorDescription);
        }
    }
}

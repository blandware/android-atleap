package com.blandware.android.atleap.sample.auth;

import android.content.Context;

import com.blandware.android.atleap.auth.BaseAuthActivity;
import com.blandware.android.atleap.auth.BaseAuthenticator;

/**
 * Created by agrebnev on 25.03.14.
 */
public class UserAuthenticator extends BaseAuthenticator {

    public UserAuthenticator(Context context) {
        super(context);
    }

    @Override
    protected Class<? extends BaseAuthActivity> getAuthActivityClass() {
        return AuthActivity.class;
    }
}

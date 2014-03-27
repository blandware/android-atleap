package com.blandware.android.atleap.sample.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by agrebnev on 25.03.14.
 */
public class UserAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        UserAuthenticator authenticator = new UserAuthenticator(this);
        return authenticator.getIBinder();
    }
}

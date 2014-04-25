package com.blandware.android.atleap.auth;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.blandware.android.atleap.BaseApplication;

/**
 * Base class for implementing an Activity that is used to help implement an
 * AbstractAccountAuthenticator. If the AbstractAccountAuthenticator needs to use an activity
 * to handle the request then it can have the activity extend AccountAuthenticatorActivity.
 * The AbstractAccountAuthenticator passes in the response to the intent using the following:
 * <pre>
 *      intent.putExtra({@link AccountManager#KEY_ACCOUNT_AUTHENTICATOR_RESPONSE}, response);
 * </pre>
 * The activity then sets the result that is to be handed to the response via
 * {@link #setAccountAuthenticatorResult(android.os.Bundle)}.
 * This result will be sent as the result of the request when the activity finishes. If this
 * is never set or if it is set to null then error {@link AccountManager#ERROR_CODE_CANCELED}
 * will be called on the response.
 */
public class SupportAccountAuthenticatorActivity extends ActionBarActivity {
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    /**
     * Set the result that is to be sent as the result of the request that caused this
     * Activity to be launched. If result is null or this method is never called then
     * the request will be canceled.
     *
     * @param result this is returned as the result of the AbstractAccountAuthenticator request
     */
    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    /**
     * Retreives the AccountAuthenticatorResponse from either the intent of the icicle, if the
     * icicle is non-zero.
     *
     * @param icicle the save instance data of this Activity, may be null
     */
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityCreated(this, icicle);
        }
    }

    /**
     * Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present.
     */
    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityStarted(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityResumed(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityPaused(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityStopped(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivitySaveInstanceState(this, outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityDestroyed(this);
        }
    }

}

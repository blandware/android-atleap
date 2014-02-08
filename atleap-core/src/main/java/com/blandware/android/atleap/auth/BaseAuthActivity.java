package com.blandware.android.atleap.auth;


import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * This is activity which should be used for Login Activity. Do not call is directly.
 * Use {@link android.accounts.AccountManager#addAccount(String, String, String[], android.os.Bundle, android.app.Activity, android.accounts.AccountManagerCallback, android.os.Handler)}
 * instead in order to add account or
 * {@link AccountManager#updateCredentials(android.accounts.Account, String, android.os.Bundle, android.app.Activity, android.accounts.AccountManagerCallback, android.os.Handler)}
 * in order to update password.
 *
 */
public abstract class BaseAuthActivity extends AccountAuthenticatorActivity {

    private static final String TAG = BaseAuthActivity.class.getSimpleName();

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TOKEN_TYPE = "AUTH_TOKEN_TYPE";

    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_PASSWORD = "PASSWORD";
    public final static String ARG_OPTIONS = "OPTIONS";

    protected AccountManager mAccountManager;
    protected String mAuthTokenType;
    protected String mAccountType;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mAccountManager = AccountManager.get(getBaseContext());

        mAccountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
        if (TextUtils.isEmpty(mAccountType))
            mAccountType = getDefaultAccountType();
        if (TextUtils.isEmpty(mAccountType)) {
            throw new IllegalArgumentException("Account type should not be empty");
        }

        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TOKEN_TYPE);
        if (TextUtils.isEmpty(mAuthTokenType))
            mAuthTokenType = getDefaultAuthTokenType();
        if (TextUtils.isEmpty(mAuthTokenType)) {
            throw new IllegalArgumentException("Auth token type should not be empty");
        }


    }

    public String getPassword() {
        return getIntent().getStringExtra(ARG_PASSWORD);
    }

    public String getAccountName() {
        return getIntent().getStringExtra(ARG_ACCOUNT_NAME);
    }

    public Bundle getAccountOptions() {
        return getIntent().getBundleExtra(ARG_OPTIONS);
    }

    protected String getDefaultAccountType() {
        return null;
    }

    protected String getDefaultAuthTokenType() {
        return null;
    }


    protected Account updateOrCreateAccount(String accountName, String password, String authToken, Bundle userData) {
        final Account account = new Account(accountName, mAccountType);
        List<Account> accounts = Arrays.asList(mAccountManager.getAccountsByType(mAccountType));
        boolean isAccountExist = accounts.contains(account);

        if (!isAccountExist) {
            Log.d(TAG, "finishLogin addAccountExplicitly");
            boolean result = mAccountManager.addAccountExplicitly(account, password, userData);
            if (!result) {
                Log.w(TAG, "Cannot create account " + account);
                return null;
            }
        }

        Log.d(TAG, "Updating account password, authToken, userData");
        mAccountManager.setAuthToken(account, mAuthTokenType, authToken);
        mAccountManager.setPassword(account, password);

        if (userData != null) {
            for (String key : userData.keySet()) {
                mAccountManager.setUserData(account, key, userData.getString(key));
            }
        }
        return account;
    }


    protected void sendCancelResult() {
        setAccountAuthenticatorResult(null);
        setResult(RESULT_CANCELED);
        finish();
    }

    protected void sendSuccessResult(Account account, String authToken) {
        Log.d(TAG, "finishLogin");

        if (account == null) {
            throw new IllegalArgumentException("account cannot be null");
        }

        Bundle authResultBundle = new Bundle();
        authResultBundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        authResultBundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        authResultBundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
        setAccountAuthenticatorResult(authResultBundle);

        Intent resultIntent = new Intent();
        resultIntent.putExtras(authResultBundle);
        setResult(RESULT_OK, resultIntent);

        finish();
    }
}

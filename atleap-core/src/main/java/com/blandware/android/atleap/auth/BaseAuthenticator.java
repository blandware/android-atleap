package com.blandware.android.atleap.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


/**
 * It is Account Authenticator. You can extend this class and register it in AndroidManifest.xml in the following way:
 *
 * <pre>
 *     {@code
 *     <uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS" />
 *     <uses-permission android:name="android.permission.MANAGE_ACCOUNTS" />
 *
 *     <service android:name="com.youcompany.yourapp.authenticator.DefaultAuthenticatorService">
 *         <intent-filter>
 *             <action android:name="android.accounts.AccountAuthenticator" />
 *         </intent-filter>
 *         <meta-data
 *             android:name="android.accounts.AccountAuthenticator"
 *             android:resource="@xml/authenticator" />
 *     </service>
 *     }
 *
 * </pre>
 *
 * the content of @xml/authenticator could looks like
 *
 * <pre>
 *     {@code
 *     <?xml version="1.0" encoding="utf-8"?>
 *     <account-authenticator xmlns:android="http://schemas.android.com/apk/res/android"
 *         android:accountType="com.youcompany.yourapp.account_type"
 *         android:icon="@drawable/ic_launcher"
 *         android:smallIcon="@drawable/ic_launcher"
 *         android:label="@string/app_name"
 *     />
 *     }
 * </pre>
 *
 * than you should create service
 *
 * <pre>
 * {@code
 * public class DefaultAuthenticatorService extends Service {
 *     @Override
 *     public IBinder onBind(Intent intent) {
 *         DefaultAuthenticator authenticator = new DefaultAuthenticator(this);
 *         return authenticator.getIBinder();
 *     }
 * }
 * </pre>
 */
public abstract class BaseAuthenticator extends AbstractAccountAuthenticator {

    private static final String TAG = BaseAuthenticator.class.getSimpleName();
    private final Context mContext;

    public BaseAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * This method should return instance of your login Activity which extends {@link com.blandware.android.atleap.auth.BaseAuthActivity}.
     * @return
     */
    protected abstract Class<? extends BaseAuthActivity> getAuthActivityClass();

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.v(TAG, "Adding account: type=" + accountType);

        return createAuthActivityIntentBundle(response, null, accountType, authTokenType, null, options);
    }

    protected Bundle createAccountManagerResult(Account account, String authToken) {
        Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
        return  result;
    }

    protected Bundle createAuthActivityIntentBundle(AccountAuthenticatorResponse response, String accountName, String accountType, String authTokenType, String password, Bundle options) {
        final Intent intent = createAuthActivityIntent(response, accountName, accountType, authTokenType, password, options);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }


    protected Intent createAuthActivityIntent(AccountAuthenticatorResponse response, String accountName, String accountType, String authTokenType, String password, Bundle options) {
        Intent intent = new Intent(mContext, getAuthActivityClass());
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(BaseAuthActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(BaseAuthActivity.ARG_AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(BaseAuthActivity.ARG_ACCOUNT_NAME, accountName);
        intent.putExtra(BaseAuthActivity.ARG_PASSWORD, password);
        intent.putExtra(BaseAuthActivity.ARG_OPTIONS, options);
        return intent;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {

        Log.v(TAG, "getAuthToken for account=" + account + " and tokenType=" + authTokenType);

        final AccountManager am = AccountManager.get(mContext);

        String authToken = am.peekAuthToken(account, authTokenType);

        if (!TextUtils.isEmpty(authToken)) {
            Log.v(TAG, "Auth token is in the cache. Retuning.");
            return createAccountManagerResult(account, authToken);
        }

        Log.v(TAG, "Trying to get password from cache");

        final String password = am.getPassword(account);
        if (TextUtils.isEmpty(authToken)) {
            if (password != null) {
                Log.v(TAG, "Password is in cache. Trying to authenticate again.");
                try {
                    authToken = authenticateOnServer(account, password, authTokenType, options, response);
                } catch (Exception e) {
                    throw new NetworkErrorException("Cannot authenticate on the server", e);
                }
            }
        }


        if (!TextUtils.isEmpty(authToken)) {
            Log.v(TAG, "Auth token was received from the server");
            return createAccountManagerResult(account, authToken);
        }


        Log.v(TAG, "Auth token was not received. Starting auth activity.");
        return createAuthActivityIntentBundle(response, account.name, account.type, authTokenType, password, options);
    }

    /**
     * Implement this method if you would like to re-authenticate automatically while getting authToken in case of the token
     * was invalidated.
     * Authentication will be with stored password.
     *
     * @param account account
     * @param password password
     * @param authTokenType authTokenType
     * @param options options
     * @param response response
     * @return authToken
     */
    protected String authenticateOnServer(Account account, String password, String authTokenType, Bundle options, AccountAuthenticatorResponse response) {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return authTokenType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
        Bundle result = new Bundle();
        boolean allowed = true;
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, allowed);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return createAuthActivityIntentBundle(response, account.name, account.type, authTokenType, null, options);
    }



}

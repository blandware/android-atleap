package com.blandware.android.atleap.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * This class help to work with {@link android.accounts.AccountManager}
 */
public class AuthHelper {

    private static final String TAG = AuthHelper.class.getSimpleName();

    /**
     * Add account. Do not use this method from main thread. Your current thread could be blocked for a long time.
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param addAccountOptions addAccountOptions, could be <code>null</code>
     * @param activity if <code>null</code> the {@link AccountManager#KEY_INTENT} will be returned, otherwise Auth Activity will be started.
     * @return
     */
    public static Bundle addAccount(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle addAccountOptions, Activity activity) {
        try {
            final AccountManager am = AccountManager.get(context);

            Bundle bundle = am.addAccount(accountType, authTokenType, requiredFeatures, addAccountOptions, activity, null, null).getResult();

            if (bundle == null)
                return null;

            return bundle;
        } catch (Exception e) {
            Log.e(TAG, "Cannot add auth account", e);
            return null;
        }
    }

    /**
     * Create account if is not exist
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param addAccountOptions addAccountOptions, could be <code>null</code>
     * @param activity cannot be null
     */
    public static void createAccountIfNotExist(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle addAccountOptions, Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }
        Account account = getFirstAccountByType(context, accountType);
        if (account == null) {
            Intent intent = getAuthActivityIntent(context, accountType, authTokenType, requiredFeatures, addAccountOptions);
            activity.startActivity(intent);
        }
    }

    /**
     * Get Intent for starting Auth Activity.
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param addAccountOptions addAccountOptions, could be <code>null</code>
     * @return Intent to start Auth Activity
     */
    public static Intent getAuthActivityIntent(Context context, String accountType, String authTokenType,  String[] requiredFeatures, Bundle addAccountOptions) {
        Bundle bundle = addAccount(context, accountType, authTokenType, requiredFeatures, addAccountOptions, null);
        if (bundle == null)
            return null;
        return bundle.getParcelable(AccountManager.KEY_INTENT);
    }

    /**
     * Get auth token. Do not use this method from main thread. Your current thread could be blocked for a long time.
     * @param context context
     * @param account account
     * @param authTokenType authTokenType
     * @param options options, could be <code>null</code>
     * @param activity if <code>null</code> the <code>null</code> could be returned (in case of there is no token), otherwise Auth Activity could be started.
     * @return authToken
     */
    public static String getAuthToken(Context context, Account account, String authTokenType, Bundle options, Activity activity) {
        try {
            final AccountManager am = AccountManager.get(context);

            Bundle bundle = am.getAuthToken(account, authTokenType, options, activity, null, null).getResult();

            if (bundle == null)
                return null;

            return bundle.getString(AccountManager.KEY_AUTHTOKEN);
        } catch (Exception e) {
            Log.e(TAG, "Cannot get auth token", e);
            return null;
        }
    }

    /**
     * Get auth token for the first account of specified type
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param options options, could be <code>null</code>
     * @param activity if <code>null</code> the <code>null</code> could be returned (in case of there is no token), otherwise Auth Activity could be started.
     * @return authToken
     */
    public static String getAuthTokenOfFirstAccount(Context context, String accountType, String authTokenType, Bundle options, Activity activity) {
        Account account = getFirstAccountByType(context, accountType);
        if (account == null) {
            if (activity != null) {
                Bundle bundle = addAccount(context, accountType, authTokenType, null, options, activity);
                if (bundle == null)
                    return null;

                return bundle.getString(AccountManager.KEY_AUTHTOKEN);
            } else {
                return null;
            }
        }
        return getAuthToken(context, account, authTokenType, options, activity);
    }

    /**
     * Get first account of specified type
     * @param context context
     * @param accountType accountType
     * @return Account, or <code>null</code> if there is no account of this type.
     */
    public static Account getFirstAccountByType(Context context, String accountType) {
        final AccountManager accountManager = AccountManager.get(context);
        Account[] account = accountManager.getAccountsByType(accountType);

        if (account == null || account.length == 0) {
            return null;
        } else {
            if (account.length > 1) {
                Log.w(TAG, "There are more than one account of this type");
            }
            return account[0];
        }
    }


    /**
     * Invalidate authToken for specified accountType
     * @param context context
     * @param accountType accountType
     * @param authToken authToken
     */
    public static void invalidateAuthToken(Context context, String accountType, String authToken) {
        final AccountManager accountManager = AccountManager.get(context);
        accountManager.invalidateAuthToken(accountType, authToken);
    }

    /**
     * Remove specified account
     * @param context context
     * @param account account to remove
     */
    public static void removeAccount(Context context, Account account) {
        final AccountManager accountManager = AccountManager.get(context);
        accountManager.removeAccount(account, null, null);
    }

}

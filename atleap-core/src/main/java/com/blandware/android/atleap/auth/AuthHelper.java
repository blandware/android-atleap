package com.blandware.android.atleap.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
     * @param options options, could be <code>null</code>
     * @param activity if <code>null</code> the {@link AccountManager#KEY_INTENT} will be returned, otherwise Auth Activity will be started.
     * @return authToken if account successfully created, otherwise <code>null</code>
     */
    public static String addAccount(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        try {
            final AccountManager am = AccountManager.get(context);

            Bundle bundle = am.addAccount(accountType, authTokenType, requiredFeatures, options, activity, null, null).getResult();

            if (bundle == null)
                return null;


            return bundle.getString(AccountManager.KEY_AUTHTOKEN);

        } catch (Exception e) {
            Log.e(TAG, "Cannot add auth account", e);
            return null;
        }
    }

    /**
     * Check if exist account, if not create one. Then check the authToken is up-to-date, if not try to authenticate.
     * The best place for this method in the begin of the onStart method of every activity.
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options addAccountOptions, could be <code>null</code>
     * @param activity cannot be null
     */
    public static void checkFirstAccountAndToken(String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }
        Account account = getFirstAccountByType(activity.getApplicationContext(), accountType);
        final AccountManager am = AccountManager.get(activity.getApplicationContext());
        if (account == null) {
            am.addAccount(accountType, authTokenType, requiredFeatures, options, activity, null, null);
        } else {
            am.getAuthToken(account, authTokenType, options, activity, null, null);
        }
    }


    /**
     * Get auth token. Do not use this method from main thread. Your current thread could be blocked for a long time.
     * @param context context
     * @param account account
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity if <code>null</code> the <code>null</code> could be returned (in case of there is no token), otherwise Auth Activity could be started.
     * @return authToken
     */
    public static String getAuthToken(Context context, Account account, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        if (isAccountExist(context, account)) {
            return getAuthTokenWithoutCheck(context, account, authTokenType, options, activity);
        } else {
            return addAccount(context, account.type, authTokenType, requiredFeatures, options, activity);
        }
    }


    private static String getAuthTokenWithoutCheck(Context context, Account account, String authTokenType, Bundle options, Activity activity) {
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
     * Get auth token for the first account of specified type. Do not use this method from main thread.
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity if <code>null</code> the <code>null</code> could be returned (in case of there is no token), otherwise Auth Activity could be started.
     * @return authToken
     */
    public static String getAuthTokenOfFirstAccount(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        Account account = getFirstAccountByType(context, accountType);
        if (account == null) {
            return addAccount(context, accountType, authTokenType, requiredFeatures, options, activity);
        }
        return getAuthTokenWithoutCheck(context, account, authTokenType, options, activity);
    }

    /**
     * Get first account of specified type.
     * @param context context
     * @param accountType accountType
     * @return Account, or <code>null</code> if there is no account of this type.
     */
    public static Account getFirstAccountByType(Context context, String accountType) {
        final AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(accountType);

        if (accounts == null || accounts.length == 0) {
            return null;
        } else {
            if (accounts.length > 1) {
                Log.w(TAG, "There are more than one account of this type");
            }
            return accounts[0];
        }
    }

    /**
     * Check if the specified account exist
     * @param context context
     * @param account account to check
     * @return <code>true</code> if account exist
     */
    public static boolean isAccountExist(Context context, Account account) {
        if (account == null)
            return false;

        final AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(account.type);

        if (accounts == null || accounts.length == 0) {
            return false;
        } else {
            for (Account a : accounts) {
                if (a.equals(account))
                    return true;
            }
            return false;
        }
    }

    /**
     * Recreate authToken for the first account of specified type.
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity, could be <code>null</code>
     */
    public static void reCreateAuthTokenForFirstAccount(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        Account account = getFirstAccountByType(context, accountType);
        reCreateAuthToken(context, account, authTokenType, requiredFeatures, options, activity);
    }

    /**
     * Recreate authToken for the specified account.
     * @param context context
     * @param account account
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity, could be <code>null</code>
     */
    public static void reCreateAuthToken(Context context, Account account, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        final AccountManager am = AccountManager.get(context);
        boolean isAccountExist = isAccountExist(context, account);
        if (!isAccountExist) {
            am.addAccount(account.type, authTokenType, requiredFeatures, options, activity, null, null);
            return;
        }

        String authToken = am.peekAuthToken(account, authTokenType);

        if (TextUtils.isEmpty(authToken)) {
            am.getAuthToken(account, authTokenType, options, activity, null, null);
            return;
        }
        am.invalidateAuthToken(account.type, authToken);
        am.getAuthToken(account, authTokenType, options, activity, null, null);
    }

    /**
     * Recreate authToken for the first account of specified type. Do not use this method from main thread.
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity, could be <code>null</code>
     */
    public static void reCreateAuthTokenForFirstAccountBlocking(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        Account account = getFirstAccountByType(context, accountType);
        reCreateAuthTokenBlocking(context, account, authTokenType, requiredFeatures, options, activity);
    }

    /**
     * Recreate authToken for the specified account. Do not use this method from main thread.
     * @param context context
     * @param account account
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity, could be <code>null</code>
     */
    public static void reCreateAuthTokenBlocking(Context context, Account account, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        boolean isAccountExist = isAccountExist(context, account);
        if (!isAccountExist) {
            addAccount(context, account.type, authTokenType, requiredFeatures, options, activity);
            return;
        }

        final AccountManager am = AccountManager.get(context);
        String authToken = am.peekAuthToken(account, authTokenType);

        if (TextUtils.isEmpty(authToken)) {
            getAuthTokenWithoutCheck(context, account, authTokenType, options, activity);
            return;
        }

        final AccountManager accountManager = AccountManager.get(context);
        accountManager.invalidateAuthToken(account.type, authToken);

        getAuthTokenWithoutCheck(context, account, authTokenType, options, activity);
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


    /**
     * Recreate first account of specified type
     * @param accountType account type
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity (cannot be <code>null</code>)
     */
    public static void reCreateFirstAccount(String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }
        Account account = getFirstAccountByType(activity.getApplicationContext(), accountType);
        if (account != null) {
            final AccountManager accountManager = AccountManager.get(activity.getApplicationContext());
            accountManager.removeAccount(account, null, null);
            accountManager.addAccount(accountType, authTokenType, requiredFeatures, options, activity, null, null);
        }
    }


    /**
     * Invalidate auth token for the first account of specified type.
     * @param accountType account type
     * @param authTokenType auth token type
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity (cannot be <code>null</code>)
     */
    public static void invalidateAuthTokenForFirstAccount(String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }
        Context context = activity.getApplicationContext();
        Account account = getFirstAccountByType(context, accountType);
        invalidateAuthToken(account, authTokenType, requiredFeatures, options, activity);
    }

    /**
     * Invalidate auth token for specified account
     * @param account account to invalidate auth token
     * @param authTokenType auth token type
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity (cannot be <code>null</code>)
     */
    public static void invalidateAuthToken(Account account, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }
        if (account == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        Context context = activity.getApplicationContext();
        final AccountManager am = AccountManager.get(context);
        String authToken = am.peekAuthToken(account, authTokenType);
        if (!TextUtils.isEmpty(authToken)) {
            am.invalidateAuthToken(account.type, authToken);
        }
        am.addAccount(account.type, authTokenType, requiredFeatures, options, activity, null, null);
    }
}

package com.blandware.android.atleap.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.blandware.android.atleap.settings.Settings;

import java.util.Map;

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
     * @return <code>true</code> if user is already authenticated
     */
    public static boolean checkLastAccountAndToken(String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }
        boolean isAuthenticated = false;
        Account account = getLastUsedAccount(activity.getApplicationContext(), accountType);
        final AccountManager am = AccountManager.get(activity.getApplicationContext());
        if (account == null) {
            am.addAccount(accountType, authTokenType, requiredFeatures, options, activity, null, null);
        } else {
            isAuthenticated = !TextUtils.isEmpty(am.peekAuthToken(account, authTokenType));
            am.getAuthToken(account, authTokenType, options, activity, null, null);
        }
        return isAuthenticated;
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
     * Get auth token for the last used account of specified type. Do not use this method from main thread.
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity if <code>null</code> the <code>null</code> could be returned (in case of there is no token), otherwise Auth Activity could be started.
     * @return authToken
     */
    public static String getAuthTokenOfLastAccount(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        Account account = getLastUsedAccount(context, accountType);
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
     * Get last used account. If there are not last used the first account of specified type will be returned.
     * <code>null</code> could be returned if there are not accounts of specified types.
     * @param context context
     * @param accountType account type
     * @return last used account. If there are not last used the first account of specified type will be returned.
     */
    public static Account getLastUsedAccount(Context context, String accountType) {

        Map<String, String> accountNamesMap = Settings.getMap(BaseAuthActivity.ACCOUNT_NAME_KEY, null);

        if (accountNamesMap == null || accountNamesMap.isEmpty()) {
            return getFirstAccountByType(context, accountType);
        } else {
            String accountName = accountNamesMap.get(accountType);
            if (TextUtils.isEmpty(accountName)) {
                return getFirstAccountByType(context, accountType);
            } else {
                return getAccount(context, accountType, accountName);
            }
        }
    }

    /**
     * Get last used account type
     * @param context context
     * @return last used account type
     */
    public static String getLastUsedAccountType(Context context, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(BaseAuthActivity.ACCOUNT_TYPE_KEY, defaultValue);
    }

    /**
     * Get account by type and name
     * @param context context
     * @param accountType account type
     * @param accountName account name
     * @return Account object
     */
    public static Account getAccount(Context context, String accountType, String accountName) {
        final AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(accountType);

        if (accounts == null || accounts.length == 0) {
            return null;
        } else {
            for (Account account: accounts) {
                if (account.name.equals(accountName)) {
                    return account;
                }
            }
            return null;
        }
    }

    /**
     * Get password of specified account
     * @param context context
     * @param account account
     * @return account password
     */
    public static String getAccountPassword(Context context, Account account) {
        if(account == null)
            return null;
        AccountManager accountManager = AccountManager.get(context);
        return accountManager.getPassword(account);
    }

    /**
     * Get user data of specified account
     * @param context context
     * @param account account
     * @param key key
     * @return user data string
     */
    public static String getUserData(Context context, Account account, String key) {
        if(account == null)
            return null;
        AccountManager accountManager = AccountManager.get(context);
        return accountManager.getUserData(account, key);
    }

    /**
     * Set user data to account
     * @param context context
     * @param account account
     * @param key key
     * @param value value
     */
    public static void setUserData(Context context, Account account, String key, String value) {
        if(account == null)
            throw new IllegalArgumentException("account cannot be null");
        AccountManager accountManager = AccountManager.get(context);
        accountManager.setUserData(account, key, value);
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
     * Recreate authToken for the last used account of specified type.
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity, could be <code>null</code>
     */
    public static void reCreateAuthTokenForLastAccount(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        Account account = getLastUsedAccount(context, accountType);
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
     * Recreate authToken for the last used account of specified type. Do not use this method from main thread.
     * @param context context
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity, could be <code>null</code>
     */
    public static void reCreateAuthTokenForLastAccountBlocking(Context context, String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        Account account = getLastUsedAccount(context, accountType);
        reCreateAuthTokenBlocking(context, account, accountType, authTokenType, requiredFeatures, options, activity);
    }

    /**
     * Recreate authToken for the specified account. Do not use this method from main thread.
     * @param context context
     * @param account account
     * @param accountType accountType
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity, could be <code>null</code>
     */
    public static void reCreateAuthTokenBlocking(Context context, Account account, String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        boolean isAccountExist = isAccountExist(context, account);
        if (!isAccountExist) {
            addAccount(context, accountType, authTokenType, requiredFeatures, options, activity);
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
     * Recreate last used account of specified type
     * @param accountType account type
     * @param authTokenType authTokenType
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity (cannot be <code>null</code>)
     */
    public static void reCreateLastAccount(String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }
        Account account = getLastUsedAccount(activity.getApplicationContext(), accountType);
        if (account != null) {
            final AccountManager accountManager = AccountManager.get(activity.getApplicationContext());
            accountManager.removeAccount(account, null, null);
            accountManager.addAccount(accountType, authTokenType, requiredFeatures, options, activity, null, null);
        }
    }


    /**
     * Invalidate auth token for the last used account of specified type.
     * @param accountType account type
     * @param authTokenType auth token type
     * @param requiredFeatures requiredFeatures, could be <code>null</code>
     * @param options options, could be <code>null</code>
     * @param activity activity (cannot be <code>null</code>)
     */
    public static void invalidateAuthTokenForLastAccount(String accountType, String authTokenType, String[] requiredFeatures, Bundle options, Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity cannot be null");
        }
        Context context = activity.getApplicationContext();
        Account account = getLastUsedAccount(context, accountType);
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

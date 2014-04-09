package com.blandware.android.atleap.sample.auth;

import android.accounts.Account;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blandware.android.atleap.auth.BaseAuthActivity;
import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.sample.exception.ServerErrorException;
import com.blandware.android.atleap.sample.model.GetAccessTokenResult;
import com.blandware.android.atleap.sample.model.User;
import com.blandware.android.atleap.sample.network.robospice.DefaultSpiceManager;
import com.blandware.android.atleap.sample.network.robospice.GetAccessTokenRequest;
import com.blandware.android.atleap.sample.network.robospice.GetUserRequest;
import com.blandware.android.atleap.sample.network.robospice.NetworkService;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by agrebnev on 25.03.14.
 */
public class AuthActivity extends BaseAuthActivity {

    private static final String TAG = AuthActivity.class.getSimpleName();

    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    public static final String KEY_USER_NAME = "KEY_USER_NAME";
    public static final String KEY_USER_COMPANY = "KEY_USER_COMPANY";
    public static final String KEY_USER_AVATAR_URL = "KEY_USER_AVATAR_URL";

    private static final String AUTHORIZE_URL = "https://github.com/login/oauth/authorize";

    private DefaultSpiceManager spiceManager = new DefaultSpiceManager(NetworkService.class);

    private WebView mWebView;

    private String mFullAuthorizeUrl;

    private String mState;

    private String mAuthToken;

    private String mScope;

    private boolean mIsAccessTokenRequestSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(TextUtils.isEmpty(getString(R.string.github_oauth_client_id)) || TextUtils.isEmpty(getString(R.string.github_oauth_client_secret))) {
            throw new IllegalStateException("Please specify github_oauth_client_id and github_oauth_client_secret in file ./src/main/res/values/settings.xml");
        }

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        setContentView(R.layout.activity_auth);

        mWebView = (WebView)findViewById(R.id.web_view);



        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                AuthActivity.this.setProgress(newProgress * 1000);
            }
        });

        mWebView.setWebViewClient(new AuthWebViewClient());

        CookieSyncManager.createInstance(this);

        mState = UUID.randomUUID().toString();


        mFullAuthorizeUrl = Uri.parse(AUTHORIZE_URL).buildUpon()
                .appendQueryParameter("client_id", getString(R.string.github_oauth_client_id))
                .appendQueryParameter("redirect_uri", getString(R.string.github_oauth_client_callback_url))
                .appendQueryParameter("scope", getString(R.string.github_oauth_scope))
                .appendQueryParameter("state", mState)
                .build()
                .toString();


        Log.v(TAG, "Staring authorization page");
        mWebView.loadUrl(mFullAuthorizeUrl);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public void onBackPressed() {
        //do not allow to go back
        moveTaskToBack(true);
    }

    @Override
    public void onStart() {
        changeProgressBarVisibility(false);
        spiceManager.start(this);
        super.onStart();


    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();

    }


    private void getAccessToken(String code) {
        Log.v(TAG, "Requesting access token");
        mIsAccessTokenRequestSent = true;
        GetAccessTokenRequest request = new GetAccessTokenRequest(
                getString(R.string.github_oauth_client_id),
                getString(R.string.github_oauth_client_secret),
                code,
                getString(R.string.github_oauth_client_callback_url)
        );
        executeSpiceRequest(request, null, 0, new GetAccessTokenCallback());
    }


    protected void changeProgressBarVisibility(boolean show) {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (progressBar != null) {
            if (show)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        }

    }



    protected DefaultSpiceManager getSpiceManager() {
        return spiceManager;
    }

    protected <T> void executeSpiceRequest(SpiceRequest<T> request, Object requestCacheKey, long cacheExpiryDuration, RequestListener<T> callback) {
        changeProgressBarVisibility(true);
        getSpiceManager().execute(request, requestCacheKey, cacheExpiryDuration, callback);
    }

    private void getCurrentUserProfile(String accessToken) {
        Log.v(TAG, "Requesting user profile information");
        executeSpiceRequest(new GetUserRequest(accessToken), null, 0, new GetCurrentUserProfileCallback());
    }

    private void finishAuth(User user, String authToken) {

        if (TextUtils.isEmpty(authToken)) {
            sendCancelResult();
        } else {
            Log.v(TAG, "Creating user account");
            Bundle options = new Bundle();
            options.putString(KEY_USER_EMAIL, user.email);
            options.putString(KEY_USER_NAME, user.name);
            options.putString(KEY_USER_ID, String.valueOf(user.id));
            options.putString(KEY_USER_COMPANY, user.company);
            options.putString(KEY_USER_AVATAR_URL, user.avatarUrl);

            Account account = updateOrCreateAccount(user.login, null, authToken, options);
            if (account != null) {
                sendSuccessResult(account, authToken);
            }
            Log.v(TAG, "User account created successfully");
        }

    }

    private class GetAccessTokenCallback implements RequestListener<GetAccessTokenResult> {
        @Override
        public void onRequestFailure(SpiceException e) {
            changeProgressBarVisibility(false);
            if (e instanceof NoNetworkException) {
                Toast.makeText(AuthActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            } else if (ExceptionUtils.indexOfType(e, ServerErrorException.class) >= 0) {
                Toast.makeText(AuthActivity.this, R.string.activity_auth_cannot_login, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onRequestSuccess(GetAccessTokenResult result) {
            changeProgressBarVisibility(false);

            mAuthToken = result.accessToken;
            mScope = result.scope;
            Log.v(TAG, "Access token received successfully");
            getCurrentUserProfile(mAuthToken);
        }


    }

    private class GetCurrentUserProfileCallback implements RequestListener<User> {
        @Override
        public void onRequestFailure(SpiceException e) {
            changeProgressBarVisibility(false);
            if (e instanceof NoNetworkException) {
                Toast.makeText(AuthActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onRequestSuccess(User result) {
            changeProgressBarVisibility(false);
            Log.v(TAG, "User profile received successfully");
            finishAuth(result, mAuthToken);
        }

    }


    private class AuthWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Uri uri = Uri.parse(url);

            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            String error = uri.getQueryParameter("error");
            String errorDescription = uri.getQueryParameter("error_description");
            if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(state)) {
                if (!state.equals(mState)) {
                    Log.w(TAG, "Codes are not equal");
                } else if (mIsAccessTokenRequestSent) {
                    Log.v(TAG, "Access token request is already sent");
                } else {
                    getAccessToken(code);
                }
                view.stopLoading();
            } else if (!TextUtils.isEmpty(error) && !TextUtils.isEmpty(errorDescription)) {
                view.stopLoading();
                Log.v(TAG, String.format("Authentication failed with code '%s' and description '%s'", error, errorDescription));
                Toast.makeText(AuthActivity.this, errorDescription, Toast.LENGTH_LONG).show();
            }

        }
    }

}

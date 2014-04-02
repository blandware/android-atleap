/*
 * Copyright (C) 2013 Blandware (http://www.blandware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blandware.android.atleap.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.protocol.HTTP;

import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

/**
 * This class contains useful method for intent creation.
 * See details http://developer.android.com/guide/components/intents-common.html
 */
public class IntentUtil {

    private static final String TAG = IntentUtil.class.getSimpleName();

    //https://dev.twitter.com/docs/tweet-button
    public static final SocialNetworkProvider TWITTER = new SocialNetworkProvider("com.twitter.android", "https://twitter.com/share?text=%s&url=%s", false);

    //https://developers.facebook.com/docs/plugins/share-button/
    public static final SocialNetworkProvider FACEBOOK = new SocialNetworkProvider("com.facebook.katana", "https://www.facebook.com/sharer/sharer.php?s=100&p[summary]=%s&p[url]=%s&p[images]=%s", true);

    //https://developers.google.com/+/web/share/?hl=en
    public static final SocialNetworkProvider GOOGLE_PLUS = new SocialNetworkProvider("com.google.android.apps.plus", "https://plus.google.com/share?fake=%s&url=%s", false);

    //https://vk.com/pages.php?act=share
    public static final SocialNetworkProvider VKONTAKTE = new SocialNetworkProvider("com.vkontakte.android", "http://vkontakte.ru/share.php?fake=%s&url=%s", false);

    //https://developers.pinterest.com/pin_it/
    public static final SocialNetworkProvider PINTEREST = new SocialNetworkProvider("com.pinterest", "https://www.pinterest.com/pin/create/button/?description=%s?url=%s&media=%s", true);

    //https://developer.linkedin.com/documents/share-linkedin
    public static final SocialNetworkProvider LINKEDIN = new SocialNetworkProvider("com.linkedin.android", "https://www.linkedin.com/shareArticle?mini=true&summary=%s&url=%s", false);

    public static final SocialNetworkProvider ODNOKLASSNIKI = new SocialNetworkProvider("ru.ok.android", "https://www.odnoklassniki.ru/dk?stcmd=addShare&st.s=1&st.comments=%s&st._surl=%s", false);

    public static final SocialNetworkProvider TUMBLR = new SocialNetworkProvider("com.tumblr", "http://www.tumblr.com/share?v=3&s=%s&u=%s", false);


    /**
     * Create Intent for sending sms.
     * @param phoneNumber telephone number
     * @param body body of sms
     * @return created intent
     */
    public static Intent sendSms(String phoneNumber, String body) {
        Intent intent = createIntent(Intent.ACTION_VIEW, null, null);
        if (!TextUtils.isEmpty(phoneNumber)) {
            intent.setData(Uri.parse("sms:"+phoneNumber));
        } else {
            intent.setData(Uri.parse("sms:"));
            //intent.setType("vnd.android-dir/mms-sms");
        }

        intent.putExtra("sms_body", body);
        return intent;
    }


    /**
     * Create intent for starting email client
     * @param to recipient
     * @param subject email subject
     * @param body body of email
     * @param attachment attachment
     * @return intent
     */
    public static Intent sendEmail(String to, String subject, String body, Uri attachment) {
        Intent intent = createIntent(Intent.ACTION_SENDTO, null, null);
        if (!TextUtils.isEmpty(to)) {
            intent.setData(Uri.parse("mailto:"+to));
        } else {
            intent.setData(Uri.parse("mailto:"));
        }
        //intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        return intent;
    }


    /**
     * Dial phone number
     * @param phoneNumber phone number
     * @return created intent
     */
    public static Intent dial(String phoneNumber) {
        Intent intent = createIntent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber), null);
        return intent;
    }

    /**
     * Call phone number. Requires permission android.permission.CALL_PHONE
     * @param phoneNumber phone number
     * @return created intent
     */
    public static Intent call(String phoneNumber) {
        Intent intent = createIntent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber), null);
        return intent;
    }


    public static Intent sendText(String text, String subject) {
        return send(text, subject, null, HTTP.PLAIN_TEXT_TYPE);
    }

    /**
     * Send action
     * @param text text
     * @param subject subject
     * @param stream file
     * @param mimeType mime type of resource. Can be <code>null</code>
     * @return created intent
     */
    public static Intent send(String text, String subject, Uri stream, String mimeType) {
        Intent intent = createIntent(Intent.ACTION_SEND, null, mimeType);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, stream);
        return intent;
    }

    /**
     * View the specified uri
     * @param uri uri to be opened
     * @return created Intent
     */
    public static Intent view(Uri uri) {
        return view(uri, null);
    }

    /**
     * View the specified uri
     * @param uri uri to be opened
     * @param mimeType mime type of resource. Can be <code>null</code>
     * @return created Intent
     */
    public static Intent view(Uri uri, String mimeType) {
        return createIntent(Intent.ACTION_VIEW, uri, mimeType);
    }


    /**
     * Create intent for specified action and data
     * @param action see actions of {@link Intent}
     * @param data data to be opened
     * @param mimeType mime type of resource. Can be <code>null</code>
     * @return created intent
     */
    public static Intent createIntent(String action, Uri data, String mimeType) {
        if (TextUtils.isEmpty(action)) {
            throw new IllegalArgumentException("action cannot be null");
        }

        Log.v(TAG, String.format("Trying to open data with path=%s and mimeType=%s", data, mimeType));


        if (TextUtils.isEmpty(mimeType) && data != null && !TextUtils.isEmpty(data.getPath())) {
            mimeType = URLConnection.guessContentTypeFromName(data.getPath());
        }
        if (!TextUtils.isEmpty(mimeType)) {
            Log.v(TAG, "We guessed that mime type is " + mimeType);
        }

        Intent intent = new Intent(action);
        if (data != null && !TextUtils.isEmpty(mimeType)) {
            intent.setDataAndType(data, mimeType);
        } else if (data != null) {
            intent.setData(data);
        } else if (!TextUtils.isEmpty(mimeType)) {
            intent.setType(mimeType);
        }
        return intent;
    }

    /**
     * Check if such intent could be started
     * @param context context it is not mandatory to be activity
     * @param intent intent for checking
     * @return <code>true</code> if specified intent could be started
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        if (intent == null) {
            return false;
        }

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * Start chooser of applications for specified intent
     * @param context context it is not mandatory to be activity
     * @param intent intent
     * @return <code>true</code> if intent started successfully
     */
    public static boolean startChooser(Context context, Intent intent) {
        if (intent == null) {
            return false;
        }

        boolean isActivity = false;
        if (context instanceof Activity) {
            isActivity = true;
        }

        if (!isActivity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }


        if (isIntentAvailable(context, intent)) {
            Intent chooserIntent = Intent.createChooser(intent, null);
            if (isActivity) {
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(chooserIntent);
            return true;
        } else {
            Log.w(TAG, String.format("Cannot find application to open path=%s and mimeType=%s", intent.getData(), intent.getType()));
            return false;
        }
    }

    /**
     * Start application based on its query name
     * @param context context it is not mandatory to be activity
     * @param query substring in package or activity name
     * @param intent intent
     * @return <code>true</code> if intent started successfully
     */
    public static boolean startApplication(Context context, String query, Intent intent) {
        if (intent == null) {
            return false;
        }

        if (context instanceof Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : activityList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo.name.toLowerCase().contains(query) || activityInfo.applicationInfo.packageName.toLowerCase().contains(query)) {
                ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(name);
                context.startActivity(intent);
                return true;
            }
        }


        return false;
    }


    /**
     * Share info via Social Network
     * @param context context it is not mandatory to be activity
     * @param text text to share
     * @param url url of resource to share, optional
     * @param imageUrl url of the image to share, optional
     * @param provider see predefined social networks providers in this class
     * @return <code>true</code> if the sharing was started successfully
     */
    public static boolean share(Context context, String text, String url, String imageUrl, SocialNetworkProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null");
        }

        if (text == null)
            text = new String();
        if (url == null)
            url = new String();
        if (imageUrl == null)
            imageUrl = new String();

        Intent appIntent;
        if (provider.isUrlInText) {
            appIntent = send(url, null, null, HTTP.PLAIN_TEXT_TYPE);
        } else {
            appIntent = send(text, null, null, HTTP.PLAIN_TEXT_TYPE);
        }

        boolean isSentViaApp = startApplication(context, provider.queryApplication, appIntent);

        if (!isSentViaApp) {
            String browserUrl = String.format(Locale.getDefault(), provider.urlFormat, new String[]{text, url, imageUrl});
            Intent intentViaBrowser = view(Uri.parse(browserUrl));
            return startChooser(context, intentViaBrowser);
        } else {
            return true;
        }
    }


    /**
     * Container for storing social network provider information
     */
    public static class SocialNetworkProvider {
        public String queryApplication;
        public String urlFormat;
        public boolean isUrlInText;

        /**
         * Create instance
         * @param queryApplication substring in package or activity name to start social network Android app
         * @param urlFormat format of URL which will be used to share via web if the Android social app in not installed
         * @param isUrlInText <code>true</code> if the URL should be passed at main text into Android Social Network app
         */
        public SocialNetworkProvider(String queryApplication, String urlFormat, boolean isUrlInText) {
            this.queryApplication = queryApplication;
            this.urlFormat = urlFormat;
            this.isUrlInText = isUrlInText;
        }
    }


}

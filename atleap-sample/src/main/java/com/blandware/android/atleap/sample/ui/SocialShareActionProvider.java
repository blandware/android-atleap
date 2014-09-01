package com.blandware.android.atleap.sample.ui;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.blandware.android.atleap.util.IntentUtil;

import java.util.ArrayList;

/**
 * Created by agrebnev on 01.09.14.
 */
public class SocialShareActionProvider extends ActionProvider implements MenuItem.OnMenuItemClickListener {

    private static ArrayList<ShareItem> SOCIAL_NETWORK_PROVIDERS = new ArrayList<ShareItem>(3);

    static {
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("Twitter", IntentUtil.TWITTER));
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("Facebook", IntentUtil.FACEBOOK));
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("Google+", IntentUtil.GOOGLE_PLUS));
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("Vkontakte", IntentUtil.VKONTAKTE));
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("Pinterest", IntentUtil.PINTEREST));
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("LinkedIn", IntentUtil.LINKEDIN));
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("Odnoklassniki", IntentUtil.ODNOKLASSNIKI));
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("Tumblr", IntentUtil.TUMBLR));
        SOCIAL_NETWORK_PROVIDERS.add(new ShareItem("Instagram", IntentUtil.INSTAGRAM));
    }

    private String mText;
    private String mUrl;
    private String mExternalResUrl;
    private Uri mLocalResUri;

    public SocialShareActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();


        for (int i = 0; i < SOCIAL_NETWORK_PROVIDERS.size(); i++) {
            ShareItem shareItem = SOCIAL_NETWORK_PROVIDERS.get(i);
            subMenu.add(0, i, i, shareItem.name).setOnMenuItemClickListener(this);
        }
    }

    public void setShareProperties(String text, String url, String externalResUrl, Uri localResUri) {
        this.mText = text;
        this.mExternalResUrl = externalResUrl;
        this.mUrl = url;
        this.mLocalResUri = localResUri;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        ShareItem shareItem = SOCIAL_NETWORK_PROVIDERS.get(item.getItemId());
        IntentUtil.SocialNetworkProvider provider = shareItem.provider;

        IntentUtil.share(getContext(), mText, mUrl, mExternalResUrl, mLocalResUri, provider);

        return true;
    }

    private static class ShareItem {
        public ShareItem(String name, IntentUtil.SocialNetworkProvider provider) {
            this.name = name;
            this.provider = provider;
        }

        public String name;
        public IntentUtil.SocialNetworkProvider provider;
    }
}

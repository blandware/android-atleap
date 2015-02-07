package com.blandware.android.atleap.sample.ui;

import android.accounts.Account;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.blandware.android.atleap.auth.AuthHelper;
import com.blandware.android.atleap.drawer.BaseDrawerFragment;
import com.blandware.android.atleap.drawer.DefaultDrawerMenuAdapter;
import com.blandware.android.atleap.drawer.DefaultDrawerMenuItem;
import com.blandware.android.atleap.sample.Constants;
import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.sample.auth.AuthActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agrebnev on 10.03.14.
 */
public class DrawerFragment extends BaseDrawerFragment {

    @Override
    public DrawerConfig getDrawerConfig() {
        DrawerConfig drawerConfig = new DrawerConfig();
        drawerConfig.drawerLayoutViewId = R.id.drawer_layout;
        drawerConfig.fragmentContainerId = R.id.navigation_drawer;
        drawerConfig.fragmentLayoutResourceId = R.layout.fragment_drawer;
        drawerConfig.drawableResourceId = R.drawable.ic_drawer;
        drawerConfig.drawerOpenStringId = R.string.fragment_drawer_open;
        drawerConfig.drawerCloseStringId = R.string.fragment_drawer_close;
        drawerConfig.drawerShadowResourceId = R.drawable.drawer_shadow;
        return drawerConfig;
    }

    @Override
    public MenuConfig getMenuConfig() {

        List<DefaultDrawerMenuItem> menuItems = new ArrayList<DefaultDrawerMenuItem>();
        menuItems.add(new DefaultDrawerMenuItem(MasterFragment.class, R.id.container, R.string.fragment_drawer_menu_search));
        menuItems.add(new DefaultDrawerMenuItem(new DefaultDrawerMenuItem.OnItemClickedCallback() {
            @Override
            public void onItemClicked(DefaultDrawerMenuItem menuItem) {
                DrawerFragment.this.logoutMenuItemClicked();
            }
        }, R.string.fragment_drawer_menu_logout));

        MenuConfig menuConfig = new MenuConfig();
        menuConfig.menuListViewId = R.id.menu_list;
        menuConfig.menuListAdapter = new DefaultDrawerMenuAdapter(
                getActivity(),
                R.layout.listitem_drawer_menu_item,
                menuItems,
                R.id.menu_title,
                0);
        return menuConfig;
    }

    @Override
    public void setMenuAdapter(ListAdapter listAdapter) {
        //set header

        View headerView = getActivity().getLayoutInflater().inflate(R.layout.fragment_drawer_header, mMenuListView, false);
        mMenuListView.addHeaderView(headerView);

        super.setMenuAdapter(listAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieSyncManager.createInstance(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Account account = AuthHelper.getLastOrFirstAccount(getActivity(), Constants.ACCOUNT_TYPE);

        ImageView userAvatarImageView = (ImageView)view.findViewById(R.id.user_avatar);
        Picasso.with(getActivity()).load(AuthHelper.getUserData(getActivity(), account, AuthActivity.KEY_USER_AVATAR_URL)).into(userAvatarImageView);

        TextView userNameTextView = (TextView)view.findViewById(R.id.user_name);
        userNameTextView.setText(AuthHelper.getUserData(getActivity(), account, AuthActivity.KEY_USER_NAME));

        TextView userLoginTextView = (TextView)view.findViewById(R.id.user_login);
        userLoginTextView.setText(account.name);

    }

    @Override
    public void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    public void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();

    }

    private void logoutMenuItemClicked() {
        AuthHelper.invalidateAuthTokenForLastAccount(Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, getActivity());
        CookieManager.getInstance().removeAllCookie();
    }

}

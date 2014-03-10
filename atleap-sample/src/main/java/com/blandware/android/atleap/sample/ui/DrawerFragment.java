package com.blandware.android.atleap.sample.ui;

import com.blandware.android.atleap.drawer.BaseDrawerFragment;
import com.blandware.android.atleap.drawer.DefaultDrawerMenuAdapter;
import com.blandware.android.atleap.drawer.DefaultDrawerMenuItem;
import com.blandware.android.atleap.sample.R;

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
        drawerConfig.drawerOpenStringId = R.string.open_drawer;
        drawerConfig.drawerCloseStringId = R.string.close_drawer;
        drawerConfig.drawerShadowResourceId = R.drawable.drawer_shadow;
        return drawerConfig;
    }

    @Override
    public MenuConfig getMenuConfig() {

        List<DefaultDrawerMenuItem> menuItems = new ArrayList<DefaultDrawerMenuItem>();
        menuItems.add(new DefaultDrawerMenuItem(MasterFragment.class, R.id.container, R.string.app_name));


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
}

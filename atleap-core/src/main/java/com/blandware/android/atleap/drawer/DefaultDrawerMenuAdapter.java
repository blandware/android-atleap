package com.blandware.android.atleap.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by agrebnev on 13.02.14.
 */
public class DefaultDrawerMenuAdapter extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected List<DefaultDrawerMenuItem> mMenuItems;
    protected int mMenuItemLayoutId;
    protected int mTitleViewId;
    protected int mIconViewId;

    public DefaultDrawerMenuAdapter(Context context, int menuItemLayoutId, List<DefaultDrawerMenuItem> menuItems, int titleViewId, int iconViewId) {
        mMenuItems = menuItems;
        mMenuItemLayoutId = menuItemLayoutId;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTitleViewId = titleViewId;
        mIconViewId = iconViewId;
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        ViewHolder holder;
        if (convertView == null) {
            v = mInflater.inflate(mMenuItemLayoutId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) v.findViewById(mTitleViewId);
            holder.iconImageView = (ImageView) v.findViewById(mIconViewId);
            v.setTag(holder);
        } else {
            v = convertView;
            holder = (ViewHolder) v.getTag();
        }

        DefaultDrawerMenuItem menuItem = (DefaultDrawerMenuItem)getItem(position);
        bindView(v ,menuItem);

        return v;
    }

    public void bindView(View v, DefaultDrawerMenuItem menuItem) {
        ViewHolder holder = (ViewHolder) v.getTag();
        holder.titleTextView.setText(menuItem.getTitleStringId());

        if (menuItem.getIconResourceId() != 0) {
            holder.iconImageView.setImageResource(menuItem.getIconResourceId());
        }
    }

    protected static class ViewHolder {
        public ImageView iconImageView;
        public TextView titleTextView;
    }
}

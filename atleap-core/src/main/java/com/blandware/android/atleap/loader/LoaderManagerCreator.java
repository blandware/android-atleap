package com.blandware.android.atleap.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;

import com.blandware.android.atleap.util.HashUtil;

/**
 * Created by agrebnev on 09.03.14.
 */
public class LoaderManagerCreator {

    public <T> LoaderManagerCreator(Fragment fragment, LoaderManager.LoaderCallbacks<T> callbacks) {
        this(fragment, callbacks, null);

    }


    public <T> LoaderManagerCreator(Fragment fragment, LoaderManager.LoaderCallbacks<T> callbacks, Bundle args) {
        this(
                fragment.getActivity().getApplicationContext(),
                fragment.getLoaderManager(),
                (int) HashUtil.longHash(((Object) fragment).getClass(), fragment.getArguments(), callbacks),
                callbacks,
                args
        );
    }

    public <T> LoaderManagerCreator(FragmentActivity activity, LoaderManager.LoaderCallbacks<T> callbacks) {
        this(activity, callbacks, null);
    }

    public <T> LoaderManagerCreator(FragmentActivity activity, LoaderManager.LoaderCallbacks<T> callbacks, Bundle args) {
        this(
                activity.getApplicationContext(),
                activity.getSupportLoaderManager(),
                (int) HashUtil.longHash(((Object) activity).getClass(), activity.getIntent(), callbacks),
                callbacks,
                args
        );
    }

    public <T> LoaderManagerCreator(Context context, LoaderManager loaderManager, int loaderId, LoaderManager.LoaderCallbacks<T> callbacks, Bundle args) {
        loaderManager.initLoader(loaderId, args, callbacks);
    }
}

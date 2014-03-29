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

package com.blandware.android.atleap.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;

import com.blandware.android.atleap.util.HashUtil;

/**
 * This class allows easily init LoaderManager for Fragment or Activity.
 */
public class LoaderManagerCreator {

    /**
     * Init LoaderManager for Fragment.
     * @param fragment fragment
     * @param callbacks Loader callbacks
     * @param <T> usually Cursor
     */
    public <T> LoaderManagerCreator(Fragment fragment, LoaderManager.LoaderCallbacks<T> callbacks) {
        this(fragment, callbacks, null);

    }

    /**
     * Init Loader Manager for fragment
     * @param fragment fragment
     * @param callbacks callbacks
     * @param args arguments for loader construction
     * @param <T>
     */
    public <T> LoaderManagerCreator(Fragment fragment, LoaderManager.LoaderCallbacks<T> callbacks, Bundle args) {
        this(
                fragment.getActivity().getApplicationContext(),
                fragment.getLoaderManager(),
                (int) HashUtil.longHash(((Object) fragment).getClass(), fragment.getArguments(), callbacks),
                callbacks,
                args
        );
    }


    /**
     * Init LoaderManager for Activity.
     * @param activity activity
     * @param callbacks Loader callbacks
     * @param <T> usually Cursor
     */
    public <T> LoaderManagerCreator(FragmentActivity activity, LoaderManager.LoaderCallbacks<T> callbacks) {
        this(activity, callbacks, null);
    }

    /**
     * Init Loader Manager for Activity
     * @param activity activity
     * @param callbacks callbacks
     * @param args arguments for loader construction
     * @param <T>
     */
    public <T> LoaderManagerCreator(FragmentActivity activity, LoaderManager.LoaderCallbacks<T> callbacks, Bundle args) {
        this(
                activity.getApplicationContext(),
                activity.getSupportLoaderManager(),
                (int) HashUtil.longHash(((Object) activity).getClass(), activity.getIntent(), callbacks),
                callbacks,
                args
        );
    }


    /**
     * Init Loader Manager
     * @param context context
     * @param loaderManager loader manager
     * @param loaderId loader id
     * @param callbacks callbacks
     * @param args arguments for loader construction
     * @param <T>
     */
    public <T> LoaderManagerCreator(Context context, LoaderManager loaderManager, int loaderId, LoaderManager.LoaderCallbacks<T> callbacks, Bundle args) {
        loaderManager.initLoader(loaderId, args, callbacks);
    }
}

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

package com.blandware.android.atleap;

import android.app.Application;
import android.content.Context;

/**
 * This class holds links to {@link android.app.Application} and {@link android.content.Context}.
 */
public class AppContext {

    private static Context sContext;
    private static Application sApplication;

    /**
     * Return context
     * @return context
     */
    public static Context getContext() {
        if (sContext == null) {
            throw new IllegalStateException("AppContext.setContext was not called in Application.onCreate() method. " +
                    "Please inherit your application from the com.blandware.android.atleap.BaseApplication class.");
        }

        return sContext;
    }

    /**
     * Usually you should set up context in the {@link android.app.Application#onCreate()}
     * @param context
     */
    public static void setContext(Context context) {
        sContext = context;
    }

    /**
     * Return link to the {@link android.app.Application}
     * @return application
     */
    public static Application getApplication() {
        if (sApplication == null) {
            throw new IllegalStateException("AppContext.setApplication was not called in Application.onCreate() method. " +
                    "Please inherit your application from the com.blandware.android.atleap.BaseApplication class.");
        }

        return sApplication;
    }

    /**
     * Usually you should set up application in the {@link android.app.Application#onCreate()}
     * @param application Application
     */
    public static void setApplication(Application application) {
        sApplication = application;
    }
}

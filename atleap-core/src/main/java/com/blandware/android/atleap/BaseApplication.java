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

/**
 * Please use this class as a base for all your Applications
 */
public class BaseApplication extends Application {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        AppContext.setApplication(this);
        AppContext.setContext(getApplicationContext());
        super.onCreate();
    }
}

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

package com.blandware.android.atleap.sample.provider;

import com.blandware.android.atleap.provider.ormlite.OrmLiteProvider;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created by agrebnev on 25.12.13.
 */
public class SampleProvider extends OrmLiteProvider {

    @Override
    protected OrmLiteSqliteOpenHelper createHelper() {
        return new SampleDatabaseHelper(getContext());
    }

    @Override
    public OrmLiteUriMatcher getUriMatcher() {
        return OrmLiteUriMatcher.getInstance(SampleUriMatcher.class, SampleContract.CONTENT_AUTHORITY);
    }

    @Override
    public String getAuthority() {
        return SampleContract.CONTENT_AUTHORITY;
    }
}

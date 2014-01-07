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

package com.blandware.android.atleap.test.robospice;

import android.app.Application;

import com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher;
import com.blandware.android.atleap.test.provider.TestContract;
import com.blandware.android.atleap.test.provider.TestDatabaseHelper;
import com.blandware.android.atleap.test.provider.TestUriMatcher;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

/**
 * Created by agrebnev on 01.01.14.
 */
public class TestRobospiceService extends SpiceService {

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        TestUriMatcher matcher = SQLiteUriMatcher.getInstance(TestUriMatcher.class, TestContract.CONTENT_AUTHORITY);

        // init
        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, TestDatabaseHelper.DATABASE_NAME, TestDatabaseHelper.DATABASE_VERSION);
        InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory( application, databaseHelper, matcher.getClassToNotificationUriMap() );
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }
}

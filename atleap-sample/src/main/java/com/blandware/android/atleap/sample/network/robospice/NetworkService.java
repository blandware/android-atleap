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

package com.blandware.android.atleap.sample.network.robospice;

import android.app.Application;

import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.blandware.android.atleap.sample.network.retrofit.ApiGithubNoAuthServices;
import com.blandware.android.atleap.sample.network.retrofit.ApiGithubServices;
import com.blandware.android.atleap.sample.network.retrofit.GithubServices;
import com.blandware.android.atleap.sample.network.retrofit.RetrofitHelper;
import com.blandware.android.atleap.sample.provider.DefaultContract;
import com.blandware.android.atleap.sample.provider.DefaultDatabaseHelper;
import com.blandware.android.atleap.sample.provider.DefaultUriMatcher;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

import retrofit.RestAdapter;

/**
 * Created by agrebnev on 22.12.13.
 */
public class NetworkService extends BaseNetworkService {

    @Override
    public void onCreate() {
        super.onCreate();
        //add retrofit interface using default restAdapter
        addRetrofitInterface(ApiGithubServices.class);

        addRetrofitInterface(ApiGithubNoAuthServices.class, RetrofitHelper.createApiGithubNoAuthRestAdapter(null).build());
        addRetrofitInterface(GithubServices.class, RetrofitHelper.createGithubRestAdapter(null).build());
    }

    protected RestAdapter.Builder createDefaultRestAdapterBuilder() {
        return RetrofitHelper.createApiGithubRestAdapter(null);
    }


    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();


        OrmLiteUriMatcher matcher = OrmLiteUriMatcher.getInstance(DefaultUriMatcher.class, DefaultContract.CONTENT_AUTHORITY);

        // init
        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, DefaultDatabaseHelper.DATABASE_NAME, DefaultDatabaseHelper.DATABASE_VERSION);
        InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory( application, databaseHelper, matcher.getClassToNotificationUriMap() );
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }


}

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

import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.blandware.android.atleap.sample.model.Repository;
import com.blandware.android.atleap.sample.model.User;

/**
 * Created by agrebnev on 29.12.13.
 */
public class DefaultUriMatcher extends OrmLiteUriMatcher {

    private static final String TABLES_SQL_REPOSITORIES_USERS =
            DefaultContract.Repository.TABLE
                    + " INNER JOIN " + DefaultContract.User.TABLE
                    + " ON (" + DefaultContract.Repository.TABLE + "." + DefaultContract.Repository._ID + "="
                    + DefaultContract.User.TABLE + "." + DefaultContract.User.REPOSITORY_ID + ")";

    public DefaultUriMatcher(String authority) {
        super(authority);
    }

    @Override
    public void instantiate() {
        addClass(DefaultContract.PATH_USERS, User.class);
        addClass(DefaultContract.PATH_USER, User.class);

        addClass(Repository.RepositoriesResult.class, DefaultContract.PATH_REPOSITORIES_USERS);
        addClass(DefaultContract.PATH_REPOSITORIES, Repository.class);
        addClass(DefaultContract.PATH_REPOSITORY, Repository.class);

        addTablesSQL(DefaultContract.PATH_REPOSITORIES_USERS, TABLES_SQL_REPOSITORIES_USERS);

    }
}

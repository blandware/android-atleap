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

package com.blandware.android.atleap.provider.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by agrebnev on 25.12.13.
 */
public abstract class OrmLiteDatabaseHelper<U extends OrmLiteUriMatcher> extends OrmLiteSqliteOpenHelper {

    private static final String TAG = OrmLiteDatabaseHelper.class.getSimpleName();

    public OrmLiteDatabaseHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    public abstract U getUriMatcher();

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            List<Class<?>> classes = getUriMatcher().getClasses();
            for (Class clazz : classes) {
                TableUtils.createTableIfNotExists(connectionSource, clazz);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Cannot create database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            List<Class<?>> classes = getUriMatcher().getClasses();
            for (Class clazz : classes) {
                TableUtils.dropTable(connectionSource, clazz, true);
                TableUtils.createTable(connectionSource, clazz);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Cannot upgrade database", e);
        }
    }

}

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

import android.util.Log;

import com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry;
import com.blandware.android.atleap.provider.sqlite.SQLiteProvider;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by agrebnev on 25.12.13.
 */
public abstract class OrmLiteProvider<H extends OrmLiteSqliteOpenHelper, U extends OrmLiteUriMatcher> extends SQLiteProvider<H, U> {

    private static final String TAG = OrmLiteProvider.class.getSimpleName();

    protected Map<Class<?>, DatabaseTableConfig> mTableConfigs = new HashMap<Class<?>, DatabaseTableConfig>();

    @Override
    public H getDatabaseHelper() {
        return this.mDatabaseHelper;
    }

    @Override
    protected String getIdFieldName(SQLiteMatcherEntry entry) {
        OrmLiteMatcherEntry ormliteEntry = (OrmLiteMatcherEntry)entry;
        if (ormliteEntry.getClazz() == null) {
            throw new IllegalStateException("In order to use ITEM type you should fill in class");
        }

        try {
            DatabaseTableConfig config = getDatabaseTableConfig(ormliteEntry.getClazz());
            FieldType[] fieldTypes = config.getFieldTypes(mDatabaseHelper.getConnectionSource().getDatabaseType());
            FieldType idFieldType = null;
            for (FieldType fieldType : fieldTypes) {
                if(fieldType.isId() || fieldType.isGeneratedId() || fieldType.isGeneratedIdSequence()) {
                    idFieldType = fieldType;
                }
            }
            if (idFieldType == null) {
                throw new IllegalStateException("Cannot find id field");
            }
            return idFieldType.getColumnName();
        } catch (SQLException ex) {
            Log.e(TAG, "Cannot get id field", ex);
            throw new IllegalStateException("Cannot find id field");
        }
    }

    protected DatabaseTableConfig getDatabaseTableConfig(Class clazz) {
        DatabaseTableConfig config = mTableConfigs.get(clazz);
        if (config == null) {
            try {
                config = DatabaseTableConfigUtil.fromClass(mDatabaseHelper.getConnectionSource(), clazz);
                config.extractFieldTypes(mDatabaseHelper.getConnectionSource());
            } catch (SQLException ex) {
                Log.e(TAG, "Cannot get table config", ex);
            }
            if (config != null) {
                mTableConfigs.put(clazz, config);
            }
        }
        return config;
    }

}

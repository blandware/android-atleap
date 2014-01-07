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

import android.net.Uri;

import com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry;
import com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by agrebnev on 25.12.13.
 */
public abstract class OrmLiteUriMatcher<E extends OrmLiteMatcherEntry> extends SQLiteUriMatcher<E> {

    private static final String TAG = OrmLiteUriMatcher.class.getSimpleName();

    protected Map<Class<?>, Uri> mClassToNotificationUri = new HashMap<Class<?>, Uri>();

    protected OrmLiteUriMatcher(String authority) {
        super(authority);
    }

    @Override
    protected E createMatcherEntry(String path) {
        return createMatcherEntry(path, null, null);
    }

    @Override
    protected E createMatcherEntry(String path, SQLiteMatcherEntry.Type baseType, String subType) {
        return (E)new OrmLiteMatcherEntry(path, baseType, subType);
    }


    public Map<Class<?>, Uri> getClassToNotificationUriMap() {
        Map<Class<?>, Uri> map = new HashMap<Class<?>, Uri>();
        for (E entry : getEntries()) {
            if(entry.getClazz() != null) {
                Uri uri = mBaseContentUri.buildUpon().appendPath(entry.getPath()).build();
                map.put(entry.getClazz(), uri);
            }
        }
        map.putAll(mClassToNotificationUri);
        return map;
    }

    public List<Class<?>> getClasses() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (OrmLiteMatcherEntry entry : getEntries()) {
            if(entry.getClazz() != null) {
                classes.add(entry.getClazz());
            }
        }
        classes.addAll(mClassToNotificationUri.keySet());
        return classes;
    }

    public void addClass(Class clazz) {
        addClass(clazz, null);
    }

    public void addClass(Class clazz, String notificationPath) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        if (mEntries.contains(clazz)) {
            throw new IllegalArgumentException("There is such class: " + clazz.toString());
        }
        Uri uri = mBaseContentUri.buildUpon().appendPath(notificationPath).build();
        mClassToNotificationUri.put(clazz, uri);
    }

    public void addClass(String path, Class clazz) {
        addClass(path, null, null, clazz);
    }

    public void addClass(String path, SQLiteMatcherEntry.Type baseType, String subType, Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        String tableName = DatabaseTableConfig.extractTableName(clazz);
        E matcherEntry = createMatcherEntry(path, baseType, subType);
        matcherEntry.setTablesSQL(tableName);
        matcherEntry.setClazz(clazz);
        addMatherEntry(matcherEntry);
    }

}

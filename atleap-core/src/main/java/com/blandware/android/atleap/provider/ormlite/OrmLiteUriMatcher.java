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
 * This class adds possibility to work with OrmLite and make mapping between Uri path and OrmLite ER model classes.
 */
public abstract class OrmLiteUriMatcher<E extends OrmLiteMatcherEntry> extends SQLiteUriMatcher<E> {

    private static final String TAG = OrmLiteUriMatcher.class.getSimpleName();

    protected Map<Class<?>, Uri> mClassToNotificationUri = new HashMap<Class<?>, Uri>();

    /**
     * {@inheritDoc}
     */
    protected OrmLiteUriMatcher(String authority) {
        super(authority);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected E createMatcherEntry(String path) {
        return createMatcherEntry(path, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected E createMatcherEntry(String path, SQLiteMatcherEntry.Type baseType, String subType) {
        return (E)new OrmLiteMatcherEntry(mAuthority, path, baseType, subType);
    }


    /**
     * Get map of classes of ER model and ContentProvider Uris.
     * This method is useful to integrate to Robospice ormlite cache persister
     * @return
     */
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

    /**
     * Get list of all registered classes
     * @return list of classes of ER model
     */
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

    /**
     * Register class of ER model.
     * @param clazz class of ER model
     */
    public void addClass(Class clazz) {
        addClass(clazz, null);
    }

    /**
     * Add class of ER model with notification path. This path will be notified when the data are changed.
     * @param clazz class of ER model
     * @param notificationPath path to notify
     */
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

    /**
     * Add mapping between Uri path and Class of ER model
     * @param path Uri path
     * @param clazz class of ER model
     */
    public void addClass(String path, Class clazz) {
        addClass(path, null, null, clazz);
    }

    /**
     * Add mapping between Uri path and Class of ER model
     * @param path Uri path
     * @param baseType base MIME type.
     * @param subType sub MIME type. See <a href="http://developer.android.com/guide/topics/providers/content-provider-creating.html#TableMIMETypes">Cursor mime types</a>
     * @param clazz class of ER model
     */
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

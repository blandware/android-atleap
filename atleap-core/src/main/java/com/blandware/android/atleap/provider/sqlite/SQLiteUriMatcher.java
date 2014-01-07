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

package com.blandware.android.atleap.provider.sqlite;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by agrebnev on 05.01.14.
 */
public abstract class SQLiteUriMatcher<E extends SQLiteMatcherEntry> {

    private static final String TAG = SQLiteUriMatcher.class.getSimpleName();

    public static volatile Map<String, SQLiteUriMatcher> instances = new HashMap<String, SQLiteUriMatcher>();

    protected String mAuthority;

    protected Uri mBaseContentUri;

    protected List<E> mEntries = new ArrayList<E>();

    protected UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static <U extends SQLiteUriMatcher> U getInstance(Class<U> clazz, String authority) {
        U matcher = (U)instances.get(authority);
        if (matcher == null) {
            synchronized (SQLiteUriMatcher.class) {
                matcher = (U)instances.get(authority);
                if (matcher == null) {
                    try {
                        matcher = clazz.getDeclaredConstructor(String.class).newInstance(authority);
                        matcher.instantiate();
                        instances.put(authority, matcher);
                    } catch (InstantiationException ex) {
                        Log.e(TAG, "Cannot create instance of " + clazz.toString(), ex);
                    } catch (IllegalAccessException ex) {
                        Log.e(TAG, "Cannot create instance of " + clazz.toString(), ex);
                    } catch (NoSuchMethodException ex) {
                        Log.e(TAG, "Cannot create instance of " + clazz.toString(), ex);
                    } catch (InvocationTargetException ex) {
                        Log.e(TAG, "Cannot create instance of " + clazz.toString(), ex);
                    } catch (Exception ex) {
                        Log.e(TAG, "Cannot create instance of " + clazz.toString(), ex);
                    }
                }
            }
        }
        return matcher;
    }


    protected SQLiteUriMatcher(String authority) {
        this.mAuthority = authority;
        this.mBaseContentUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(authority).build();
    }

    public abstract void instantiate();

    public void addTablesSQL(String path, String tablesSQL) {
        addTablesSQL(path, null, null, tablesSQL);
    }

    public void addTablesSQL(String path, SQLiteMatcherEntry.Type baseType, String subType, String tablesSQL) {
        E matcherEntry = createMatcherEntry(path, baseType, subType);
        matcherEntry.setTablesSQL(tablesSQL);
        addMatherEntry(matcherEntry);
    }

    public void addRawSQL(String path, String rawSQL) {
        addRawSQL(path, null, null, rawSQL);
    }

    public void addRawSQL(String path, SQLiteMatcherEntry.Type baseType, String subType, String rawSQL) {
        E matcherEntry = createMatcherEntry(path, baseType, subType);
        matcherEntry.setRawSQL(rawSQL);
        addMatherEntry(matcherEntry);
    }

    public void addSQLBuilderCallback(String path, SQLiteMatcherEntry.SQLBuilderCallback callback) {
        addSQLBuilderCallback(path, null, null, callback);
    }

    public void addSQLBuilderCallback(String path, SQLiteMatcherEntry.Type baseType, String subType, SQLiteMatcherEntry.SQLBuilderCallback callback) {
        E matcherEntry = createMatcherEntry(path, baseType, subType);
        matcherEntry.setCallback(callback);
        addMatherEntry(matcherEntry);
    }

    protected E createMatcherEntry(String path) {
        return createMatcherEntry(path, null, null);
    }

    protected E createMatcherEntry(String path, SQLiteMatcherEntry.Type baseType, String subType) {
        return (E)new SQLiteMatcherEntry(path, baseType, subType);
    }

    public void addMatherEntry(E matcherEntry) {
        if (TextUtils.isEmpty(matcherEntry.getPath())) {
            throw new IllegalArgumentException("Path cannot be empty");
        }

        if (mEntries.contains(matcherEntry)) {
            throw new IllegalArgumentException("There is such path: " + matcherEntry.getPath());
        }

        getEntries().add(matcherEntry);

        int index = getEntries().indexOf(matcherEntry);

        mUriMatcher.addURI(mAuthority, matcherEntry.getPath(), index);

    }


    public String getType(Uri uri) {
        E entry = getMatcherEntry(uri);
        return entry.getBaseType().getType() + "/" + entry.getSubType();
    }

    protected List<E> getEntries() {
        return mEntries;
    }

    public E getMatcherEntry(Uri uri) {
        int match = mUriMatcher.match(uri);

        if (match == UriMatcher.NO_MATCH) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        E matcherEntry = getEntries().get(match);

        if (matcherEntry == null) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return matcherEntry;
    }

}

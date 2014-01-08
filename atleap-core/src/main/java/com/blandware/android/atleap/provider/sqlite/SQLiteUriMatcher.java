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
 * This is matcher between Uri path of ContentProvider and SQL expressions. Use {@link SQLiteUriMatcher#getInstance}
 * method to receive instance of this class
 */
public abstract class SQLiteUriMatcher<E extends SQLiteMatcherEntry> {

    private static final String TAG = SQLiteUriMatcher.class.getSimpleName();

    public static volatile Map<String, SQLiteUriMatcher> instances = new HashMap<String, SQLiteUriMatcher>();

    protected String mAuthority;

    protected Uri mBaseContentUri;

    protected List<E> mEntries = new ArrayList<E>();

    protected UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * This is thread-safe singleton. It returns different instances for different authorities.
     * @param clazz class of {@link SQLiteUriMatcher} child.
     * @param authority auhtority of {@link android.content.ContentProvider}
     * @param <U> type of {@link SQLiteUriMatcher}
     * @return instance of this class
     */
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

    /**
     * This method called right after constructor. Here you should call 'add' methods to make mapping.
     * @see com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher#addTablesSQL(String, com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry.Type, String, String)
     * @see com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher#addTablesSQL(String, String)
     * @see com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher#addRawSQL(String, String)
     * @see com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher#addRawSQL(String, com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry.Type, String, String)
     * @see com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher#addSQLBuilderCallback(String, com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry.Type, String, com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry.SQLBuilderCallback)
     * @see com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher#addSQLBuilderCallback(String, com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry.SQLBuilderCallback)
     * @see com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher#addMatherEntry(SQLiteMatcherEntry)
     */
    public abstract void instantiate();

    /**
     * Add mapping between Uri path and SQL which represent one of more (using INNER JOIN or LEFT OUTER JOIN) tables.
     * The base type and sub type will be guessed.
     * @param path Uri path
     * @param tablesSQL sql string
     */
    public void addTablesSQL(String path, String tablesSQL) {
        addTablesSQL(path, null, null, tablesSQL);
    }

    /**
     * Add mapping between Uri path and SQL which represent one of more (using INNER JOIN or LEFT OUTER JOIN) tables.
     * @param path Uri path
     * @param baseType base MIME type.
     * @param subType sub MIME type. See <a href="http://developer.android.com/guide/topics/providers/content-provider-creating.html#TableMIMETypes">Cursor mime types</a>
     * @param tablesSQL
     */
    public void addTablesSQL(String path, SQLiteMatcherEntry.Type baseType, String subType, String tablesSQL) {
        E matcherEntry = createMatcherEntry(path, baseType, subType);
        matcherEntry.setTablesSQL(tablesSQL);
        addMatherEntry(matcherEntry);
    }

    /**
     * Add mapping between Uri path and raw SQL.
     * It could contains 3 formatter specifiers like %s for projection, selection and sortOrder respectively.
     * The base type and sub type will be guessed.
     * @param path uri path
     * @param rawSQL raw SQL string
     */
    public void addRawSQL(String path, String rawSQL) {
        addRawSQL(path, null, null, rawSQL);
    }

    /**
     * Add mapping between Uri path and raw SQL.
     * It could contains 3 formatter specifiers like %s for projection, selection and sortOrder respectively.
     * @param path uri path
     * @param baseType base MIME type.
     * @param subType sub MIME type. See <a href="http://developer.android.com/guide/topics/providers/content-provider-creating.html#TableMIMETypes">Cursor mime types</a>
     * @param rawSQL raw SQL string
     */
    public void addRawSQL(String path, SQLiteMatcherEntry.Type baseType, String subType, String rawSQL) {
        E matcherEntry = createMatcherEntry(path, baseType, subType);
        matcherEntry.setRawSQL(rawSQL);
        addMatherEntry(matcherEntry);
    }

    /**
     * Add mapping between Uri path and callback method which builds raw SQL string.
     * @param path Uri path
     * @param callback callback method
     */
    public void addSQLBuilderCallback(String path, SQLiteMatcherEntry.SQLBuilderCallback callback) {
        addSQLBuilderCallback(path, null, null, callback);
    }

    /**
     * Add mapping between Uri path and callback method which builds raw SQL string.
     * @param path Uri path
     * @param baseType base MIME type.
     * @param subType sub MIME type. See <a href="http://developer.android.com/guide/topics/providers/content-provider-creating.html#TableMIMETypes">Cursor mime types</a>
     * @param callback callback method
     */
    public void addSQLBuilderCallback(String path, SQLiteMatcherEntry.Type baseType, String subType, SQLiteMatcherEntry.SQLBuilderCallback callback) {
        E matcherEntry = createMatcherEntry(path, baseType, subType);
        matcherEntry.setCallback(callback);
        addMatherEntry(matcherEntry);
    }

    protected E createMatcherEntry(String path) {
        return createMatcherEntry(path, null, null);
    }

    protected E createMatcherEntry(String path, SQLiteMatcherEntry.Type baseType, String subType) {
        return (E)new SQLiteMatcherEntry(mAuthority, path, baseType, subType);
    }

    /**
     * Add {@link com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry} which represent mapping between Uri path
     * and SQL.
     * @param matcherEntry
     */
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


    /**
     * Get full MIME type for the specified URI
     * @param uri uri
     * @return full MIME type
     */
    public String getType(Uri uri) {
        E entry = getMatcherEntry(uri);
        return entry.getBaseType().getType() + "/" + entry.getSubType();
    }

    protected List<E> getEntries() {
        return mEntries;
    }

    /**
     * Get {@link com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry} for the specified Uri
     * @param uri uri
     * @return matcher entry
     */
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

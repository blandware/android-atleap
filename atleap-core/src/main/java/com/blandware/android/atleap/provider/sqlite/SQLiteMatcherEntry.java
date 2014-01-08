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
import android.net.Uri;
import android.text.TextUtils;

/**
 * It is POJO for the SQLiteUriMatcher. It holds mapping data between uri mPath and SQL.
 */
public class SQLiteMatcherEntry {

    private static final String TAG = SQLiteMatcherEntry.class.getSimpleName();

    protected static final String TYPE_PREFIX = "vnd.";

    private String mAuthority;
    private String mPath;
    private Type mBaseType;
    private String mSubType;
    private String mTablesSQL;
    private String mRawSQL;
    private SQLBuilderCallback mCallback;

    /**
     * Create entry based on Uri mPath. The mPath can contain wildcards (* and #). See {@link android.content.UriMatcher} for details.
     * BaseType will be guessed based on mPath. SubType will be guessed based on BaseType.
     * @param authority authority of the provider
     * @param path Uri mPath
     */
    public SQLiteMatcherEntry(String authority, String path) {
        this(authority, path, null, null);
    }

    /**
     * Create entry based on Uri mPath. The mPath can contain wildcards (* and #). See {@link android.content.UriMatcher} for details.
     * @param authority authority of the provider
     * @param path uri mPath
     * @param baseType base mime type
     * @param subType sub mime type
     */
    public SQLiteMatcherEntry(String authority, String path, Type baseType, String subType) {
        if (TextUtils.isEmpty(authority)) {
            throw new IllegalArgumentException("authority cannot be empty");
        }

        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path cannot be empty");
        }
        this.mAuthority = authority;
        this.mPath = path;

        if (baseType == null) {
            if (path.endsWith("*") || path.endsWith("#")) {
                this.mBaseType = Type.ITEM;
            } else {
                this.mBaseType = Type.DIR;
            }
        } else {
            this.mBaseType = baseType;
        }

        if (TextUtils.isEmpty(subType)) {
            this.mSubType = makeSubType();
        } else {
            this.mSubType = subType;
        }

    }

    public String getAuthority() {
        return mAuthority;
    }

    public String getPath() {
        return mPath;
    }

    public Type getBaseType() {
        return mBaseType;
    }

    public String getSubType() {
        return mSubType;
    }

    public String getTablesSQL() {
        return mTablesSQL;
    }

    public void setTablesSQL(String tablesSQL) {
        if (!TextUtils.isEmpty(mRawSQL) || mCallback != null) {
            throw new IllegalStateException("Only one SQL could be setup: either tablesSQL or rawSQL or callback");
        }
        this.mTablesSQL = tablesSQL;
    }

    public String getRawSQL() {
        return mRawSQL;
    }

    public void setRawSQL(String rawSQL) {
        if (!TextUtils.isEmpty(mTablesSQL) || mCallback != null) {
            throw new IllegalStateException("Only one SQL could be setup: either tablesSQL or rawSQL or callback");
        }
        this.mRawSQL = rawSQL;
    }

    public SQLBuilderCallback getCallback() {
        return mCallback;
    }

    public void setCallback(SQLBuilderCallback callback) {
        if (!TextUtils.isEmpty(mTablesSQL) || !TextUtils.isEmpty(mRawSQL)) {
            throw new IllegalStateException("Only one SQL could be setup: either tablesSQL or rawSQL or callback");
        }
        this.mCallback = callback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLiteMatcherEntry that = (SQLiteMatcherEntry) o;
        if (!mPath.equals(that.mPath)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return mPath.hashCode();
    }

    protected String makeSubType() {
        String cleanPath = mPath.replaceAll("\\*", ".").replaceAll("#", ".").replaceAll("\\/", ".");
        return new StringBuilder(TYPE_PREFIX).append(mAuthority).append(".").append(cleanPath).toString();
    }

    /**
     * Base MIME type. See {@link ContentResolver#CURSOR_ITEM_BASE_TYPE} and {@link ContentResolver#CURSOR_DIR_BASE_TYPE}.
     */
    public enum Type {
        ITEM, DIR;

        public String getType() {
            if (this.equals(ITEM)) {
                return ContentResolver.CURSOR_ITEM_BASE_TYPE;
            } else if (this.equals(DIR)) {
                return ContentResolver.CURSOR_DIR_BASE_TYPE;
            }
            return null;
        }
    }

    /**
     * Callback to the SQL builder. It allows you to build your SQL in flexible way based on uri and other params
     */
    public interface SQLBuilderCallback {
        public String getRawSQL(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);
    }
}

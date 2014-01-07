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
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.blandware.android.atleap.AppContext;

/**
 * It is POJO for the SQLiteUriMatcher. It holds mapping data between uri path and SQL.
 */
public class SQLiteMatcherEntry {

    private static final String TAG = SQLiteMatcherEntry.class.getSimpleName();

    protected static final String TYPE_PREFIX = "vnd.";
    protected static final String DIR_SUFFIX = ".dir";
    protected static final String ITEM_SUFFIX = ".item";

    private String path;
    private Type baseType;
    private String subType;
    private String tablesSQL;
    private String rawSQL;
    private SQLBuilderCallback callback;

    /**
     * Create entry based on Uri path. The path can contain wildcards (* and #). See UriMatcher for details.
     * BaseType will be guessed based on path. SubType will be guessed based on BaseType.
     * @param path Uri path
     */
    public SQLiteMatcherEntry(String path) {
        this(path, null, null);
    }

    /**
     * Create entry based on Uri path. The path can contain wildcards (* and #). See UriMatcher for details.
     * @param path uri path
     * @param baseType base mime type
     * @param subType sub mime type
     */
    public SQLiteMatcherEntry(String path, Type baseType, String subType) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path cannot be empty");
        }

        this.path = path;

        if (baseType == null) {
            if (path.endsWith("*") || path.endsWith("#")) {
                this.baseType = Type.ITEM;
            } else {
                this.baseType = Type.DIR;
            }
        } else {
            this.baseType = baseType;
        }

        if (TextUtils.isEmpty(subType)) {
            this.subType = makeSubType(this.path, this.baseType);
        } else {
            this.subType = subType;
        }

    }

    public String getPath() {
        return path;
    }

    public Type getBaseType() {
        return baseType;
    }

    public String getSubType() {
        return subType;
    }

    public String getTablesSQL() {
        return tablesSQL;
    }

    public void setTablesSQL(String tablesSQL) {
        this.tablesSQL = tablesSQL;
    }

    public String getRawSQL() {
        return rawSQL;
    }

    public void setRawSQL(String rawSQL) {
        this.rawSQL = rawSQL;
    }

    public SQLBuilderCallback getCallback() {
        return callback;
    }

    public void setCallback(SQLBuilderCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLiteMatcherEntry that = (SQLiteMatcherEntry) o;
        if (!path.equals(that.path)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    protected String getPackageName() {
        Context context = AppContext.getContext();
        if (context != null) {
            return context.getPackageName();
        } else {
            return this.getClass().getPackage().getName();
        }
    }

    protected String makeSubType(String path, Type baseType) {
        String cleanPath = path.replaceAll("\\*", ".").replaceAll("#", ".").replaceAll("\\/", ".");
        if (baseType.equals(Type.DIR)) {
            return TYPE_PREFIX + cleanPath + "." + getPackageName() + DIR_SUFFIX;
        } else {
            return TYPE_PREFIX + cleanPath + "." + getPackageName() + ITEM_SUFFIX;
        }
    }

    /**
     * Base mime type. See ContentResolver.CURSOR_ITEM_BASE_TYPE and ContentResolver.CURSOR_DIR_BASE_TYPE.
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

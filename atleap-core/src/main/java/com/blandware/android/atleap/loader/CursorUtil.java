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

package com.blandware.android.atleap.loader;

import android.content.Context;
import android.database.Cursor;

/**
 * This class contains methods which helps to work with Cursor and Database
 */
public class CursorUtil {

    /**
     * Get table column names from the cursor
     * @param cursor
     * @return array of the field names
     */
    protected static String[] getFieldNames(Cursor cursor) {
        if (cursor == null)
            return null;

        return cursor.getColumnNames();
    }

    /**
     * Find the resource id for the views located on the layout
     * @param fieldNames column names of the DB table
     * @return array of the layout resource id
     */
    protected static int[] getLayoutViewIds(Context context, String[] fieldNames) {
        if (fieldNames == null || fieldNames.length == 0)
            return null;

        int[] layoutViewIds = new int[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            int id = context.getResources().getIdentifier(fieldName, "id", context.getPackageName());
            layoutViewIds[i] = id;
        }

        return layoutViewIds;
    }
}

package com.blandware.android.atleap.loader;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by agrebnev on 10.03.14.
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

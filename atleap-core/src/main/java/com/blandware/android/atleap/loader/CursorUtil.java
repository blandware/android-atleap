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
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains methods which helps to work with Cursor and Database
 */
public class CursorUtil {

    private static final String TAG = CursorUtil.class.getSimpleName();

    private static final String BEAN_DELIM = ".";

    /**
     * Get table column names from the cursor
     * @param cursor
     * @return array of the field names
     */
    public static String[] getFieldNames(Cursor cursor) {
        if (cursor == null)
            return null;

        return cursor.getColumnNames();
    }

    /**
     * Find the resource id for the views located on the layout
     * @param fieldNames column names of the DB table
     * @return array of the layout resource id
     */
    public static int[] getLayoutViewIds(Context context, String[] fieldNames) {
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

    public static <T extends Map<String, Object>, E> List<T> listOfBeansToListOfMaps(List<E> data, String[] fieldNames) {
        List<T> listOfMaps = new ArrayList<T>(data.size());
        for(Object bean : data) {
            T map = beanToMap(bean, fieldNames);
            listOfMaps.add(map);
        }
        return listOfMaps;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Map<String, Object>> T beanToMap(Object bean, String[] fieldNames) {
        Map<String, Object> map = new HashMap<String, Object>(fieldNames.length);
        for(String fieldName:fieldNames) {
            Object value = getValue(bean, fieldName);
            if (value != null)
                map.put(fieldName, value);
        }
        return (T)map;
    }

    private static Object getValue(Object bean, String fieldName) {
        Object result = null;
        String restFieldName = null;

        String propertyName = fieldName;
        if (fieldName.contains(BEAN_DELIM)) {
            propertyName = fieldName.substring(0, fieldName.indexOf(BEAN_DELIM) - 1);
            restFieldName = fieldName.substring(fieldName.indexOf(BEAN_DELIM), fieldName.length());
        }

        try {

            Field field = getField(bean, propertyName);
            if (field != null) {
                result = field.get(bean);
            } else {
                Method method = getMethod(bean, propertyName);
                if (method != null) {
                    result = method.invoke(bean);
                } else {
                    Log.w(TAG, "Cannot find method/field with name " + propertyName + " in class " + bean.getClass().getCanonicalName());
                    return null;
                }
            }

            if (restFieldName != null) {
                result = getValue(result, restFieldName);
            }

        } catch (IllegalAccessException ex) {
            Log.w(TAG, "Cannot invoke method/field with name " + propertyName + " in class " + bean.getClass().getCanonicalName(), ex);
        } catch (InvocationTargetException ex) {
            Log.w(TAG, "Cannot invoke method/field with name " + propertyName + " in class " + bean.getClass().getCanonicalName(), ex);
        }

        return result;

    }

    private static Field getField(Object bean, String property) {
        for(Field field :bean.getClass().getFields()) {
            if (field.getName().toLowerCase().equals(property.toLowerCase())) {
                return field;
            }
        }
        return null;
    }

    private static Method getMethod(Object bean, String property) {
        for(Method method :bean.getClass().getMethods()) {
            if (method.getName().toLowerCase().equals(property.toLowerCase()) || method.getName().toLowerCase().equals("get"+property.toLowerCase()) || method.getName().toLowerCase().equals("is"+property.toLowerCase())) {
                return method;
            }
        }
        return null;
    }

}

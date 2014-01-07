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

package com.blandware.android.atleap.util;

import android.text.TextUtils;

/**
 * This class contains set of methods useful during working with Strings
 */
public class StringUtil {

    private static final String DEFAULT_DELIM = ",";

    /**
     * Convert String to Array of Strings using default delimiter (",")
     * @param string input string
     * @return array of strings
     */
    public static String[] strToArray(String string) {
        return strToArray(string, DEFAULT_DELIM);
    }

    /**
     * Convert String to Array of Strings
     * @param string input string
     * @param delim delimiter of strings
     * @return array of strings
     */
    public static String[] strToArray(String string, String delim) {
        if (TextUtils.isEmpty(delim)) {
            throw new IllegalArgumentException("delim cannot be empty");
        }
        if(string == null) {
            return null;
        }
        if (string.length() == 0) {
            return new String[]{};
        }

        return string.split(delim);
    }

    /**
     * Convert array of Strings to String using default delimiter (",")
     * @param array array of strings
     * @return string
     */
    public static String arrayToStr(String[] array) {
        return arrayToStr(array, DEFAULT_DELIM);
    }

    /**
     * Convert array of Strings to String
     * @param array array of strings
     * @param delim delimiter
     * @return string
     */
    public static String arrayToStr(String[] array, String delim) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            String s = array[i];
            if (i > 0) {
                builder.append(delim);
            }
            builder.append(s);
        }
        return builder.toString();

    }
}

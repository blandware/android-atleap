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

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class contains functions to calculate hash using sha-1 algorithm
 */
public class HashUtil {

    protected static final String ALGORITHM = "sha-1";
    private static final String TAG = HashUtil.class.getSimpleName();

    /**
     * Calculate hash based on String values of input params
     * @param value input parameters
     * @return hash long value
     */
    public static long longHash(Object... value) {
        try {
            StringBuilder inputString = new StringBuilder();
            for (Object object : value) {
                inputString.append(String.valueOf(object));
            }
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(inputString.toString().getBytes());
            byte[] hashBytes = digest.digest();
            long hashLong = 0;
            for( int i = 0; i < 8; i++ ) {
                hashLong |= ((long)(hashBytes[i]) & 0x0FF)<<(8*i);
            }
            return hashLong;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Cannot generate hash value", e);
            return 0l;
        }
    }

}

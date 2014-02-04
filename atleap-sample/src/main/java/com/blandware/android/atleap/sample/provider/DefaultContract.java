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

package com.blandware.android.atleap.sample.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by agrebnev on 29.12.13.
 */
public class DefaultContract {

    private DefaultContract() {}

    public static final String CONTENT_AUTHORITY = "com.blandware.android.atleap.sample.authority";

    public static final Uri BASE_CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(CONTENT_AUTHORITY).build();

    public static final String PATH_USERS = "users";
    public static final String PATH_USER = "users/#";

    public static class User implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        public static final String TABLE = "user";

        public static final String LOGIN = "login";
        public static final String AVATAR_URL = "avatar_url";
        public static final String HTML_URL = "html_url";
        public static final String REPOSITORY_ID = "repository_id";
    }


    public static final String PATH_REPOSITORIES = "repositories";
    public static final String PATH_REPOSITORY = "repositories/#";

    public static class Repository implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPOSITORIES).build();

        public static final String TABLE = "repository";

        public static final String NAME = "name";
        public static final String FULL_NAME = "full_name";
        public static final String HTML_URL = "html_url";
        public static final String DESCRIPTION = "description";
        public static final String STARGAZERS_COUNT = "stargazers_count";
        public static final String CREATED_AT = "created_at";
        public static final String OWNER_ID = "owner_id";
    }


    public static final String PATH_REPOSITORIES_USERS = "repositories_users";
    public static final Uri CONTENT_URI_REPOSITORIES_USERS =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPOSITORIES_USERS).build();
}

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

package com.blandware.android.atleap.sample.model;

/**
 * Created by agrebnev on 22.12.13.
 */

import com.blandware.android.atleap.sample.provider.DefaultContract;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = DefaultContract.User.TABLE)
public class User {

    @DatabaseField(id = true, columnName = DefaultContract.User._ID)
    public int id;

    @DatabaseField(columnName = DefaultContract.User.LOGIN)
    public String login;

    @JsonProperty("avatar_url")
    @DatabaseField(columnName = DefaultContract.User.AVATAR_URL)
    public String avatarUrl;

    @JsonProperty("html_url")
    @DatabaseField(columnName = DefaultContract.User.HTML_URL)
    public String htmlUrl;

    @DatabaseField(columnName = DefaultContract.User.NAME)
    public String name;

    @DatabaseField(columnName = DefaultContract.User.COMPANY)
    public String company;

    @DatabaseField(columnName = DefaultContract.User.EMAIL)
    public String email;

    @DatabaseField(foreign = true, columnName = DefaultContract.User.REPOSITORY_ID)
    public Repository repository;


}

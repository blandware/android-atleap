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

import com.blandware.android.atleap.sample.provider.SampleContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

@DatabaseTable(tableName = SampleContract.Contributor.TABLE)
public class Contributor {

    @DatabaseField(generatedId = true, columnName = SampleContract.Contributor._ID)
    private int id;

    @DatabaseField(columnName = SampleContract.Contributor.LOGIN)
    public String login;

    @DatabaseField(columnName = SampleContract.Contributor.CONTRIBUTIONS)
    public int contributions;

    @DatabaseField(foreign = true)
    private ContributorsResult result;

    @SuppressWarnings("serial")
    @DatabaseTable(tableName = "contributor_list")
    public static class ContributorsResult extends ArrayList<Contributor> {

        @DatabaseField(id = true)
        private int id = 0;


        @ForeignCollectionField(eager = false)
        private Collection<Contributor> result = this;

        public Collection<Contributor> getResult() {
            return result;
        }

        public void setResult(Collection<Contributor> result) {
            if (result != null) {
                this.clear();
                this.addAll(result);
            }
        }

    }


}

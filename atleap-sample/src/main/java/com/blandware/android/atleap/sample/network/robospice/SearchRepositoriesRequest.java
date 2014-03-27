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

package com.blandware.android.atleap.sample.network.robospice;

/**
 * Created by agrebnev on 22.12.13.
 */
import com.blandware.android.atleap.sample.model.Repository;
import com.blandware.android.atleap.sample.network.retrofit.ApiGithubServices;


public class SearchRepositoriesRequest extends BaseRequest<Repository.RepositoriesResult, ApiGithubServices> {

    private String query;
    private int page;
    private int perPage;

    public SearchRepositoriesRequest(String query,
                                     int page,
                                     int perPage) {
        super(Repository.RepositoriesResult.class, ApiGithubServices.class);
        this.query = query;
        this.page = page;
        this.perPage = perPage;
    }

    @Override
    public Repository.RepositoriesResult loadDataFromNetwork() {
        return getService().searchRepositories(query, page, perPage);
    }
}

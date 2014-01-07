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

package com.blandware.android.atleap.sample.network;

/**
 * Created by agrebnev on 22.12.13.
 */
import com.blandware.android.atleap.sample.model.Contributor;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;


public class ContributorRequest extends RetrofitSpiceRequest<Contributor.ContributorsResult, GitHub> {

    private String owner;
    private String repo;

    public ContributorRequest(String owner, String repo) {
        super(Contributor.ContributorsResult.class, GitHub.class);
        this.owner = owner;
        this.repo = repo;
    }

    @Override
    public Contributor.ContributorsResult loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().contributors(owner, repo);
    }
}

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

package com.blandware.android.atleap.sample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.sample.network.ContributorRequest;
import com.blandware.android.atleap.sample.provider.SampleContract;
import com.blandware.android.atleap.util.ListLoader;
import com.octo.android.robospice.persistence.DurationInMillis;

/**
 * Created by agrebnev on 22.12.13.
 */
public class MasterFragment extends BaseFragment {

    private static final String TAG = MasterFragment.class.getSimpleName();

    private ContributorRequest githubRequest;


    public static MasterFragment newInstance() {
        return new MasterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListLoader listLoader = new ListLoader(
                this,
                R.id.contributors,
                R.layout.listitem_contributor,
                SampleContract.Contributor.CONTENT_URI
        );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        githubRequest = new ContributorRequest("octo-online", "robospice");
    }

    @Override
    public void onStart() {
        super.onStart();

        getSpiceManager().execute(githubRequest, "github", DurationInMillis.ONE_MINUTE, null);
    }



}

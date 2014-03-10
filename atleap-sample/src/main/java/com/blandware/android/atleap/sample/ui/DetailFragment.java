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

import com.blandware.android.atleap.loader.LoaderManagerCreator;
import com.blandware.android.atleap.loader.ViewLoadable;
import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.sample.provider.DefaultContract;

/**
 * Created by agrebnev on 22.12.13.
 */
public class DetailFragment extends BaseFragment {

    private static final String TAG = DetailFragment.class.getSimpleName();

    public static final String ARG_REPOSITORY_ID = "ARG_REPOSITORY_ID";

    protected int mRepositoryId;

    public static DetailFragment newInstance(int repositoryId) {
        DetailFragment fragment =  new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_REPOSITORY_ID, repositoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null || getArguments().getInt(ARG_REPOSITORY_ID, -1) == -1) {
            throw new IllegalArgumentException("ARG_REPOSITORY_ID cannot be null");
        }

        mRepositoryId = getArguments().getInt(ARG_REPOSITORY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewLoadable viewLoadable = new ViewLoadable(getActivity(), DefaultContract.Repository.getRepositoryUri(mRepositoryId), getView());
        LoaderManagerCreator loaderManagerCreator = new LoaderManagerCreator(this, viewLoadable);

    }

}

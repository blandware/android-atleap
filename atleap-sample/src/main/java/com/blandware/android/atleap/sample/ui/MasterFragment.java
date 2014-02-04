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

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.sample.network.SearchRepositoriesRequest;
import com.blandware.android.atleap.sample.provider.DefaultContract;
import com.blandware.android.atleap.util.ListLoader;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.squareup.picasso.Picasso;

/**
 * Created by agrebnev on 22.12.13.
 */
public class MasterFragment extends BaseFragment {

    private static final String TAG = MasterFragment.class.getSimpleName();

    private static final String QUERY = "android+language:java";
    private static final int PAGE = 1;
    private static final int PER_PAGE = 20;

    private static final String CACHE_KEY = "cache_key";


    private SearchRepositoriesRequest searchRepositoriesRequest;


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
                R.id.list_repositories,
                R.layout.listitem_repository,
                DefaultContract.CONTENT_URI_REPOSITORIES_USERS
        );
        listLoader.getAdapter().setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (view.getId() == R.id.avatar_url) {
                    ImageView imageView = (ImageView)view;
                    String avatarUrl = cursor.getString(i);
                    Picasso.with(getActivity()).load(avatarUrl).into(imageView);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchRepositoriesRequest = new SearchRepositoriesRequest(QUERY, PAGE, PER_PAGE);
    }

    @Override
    public void onStart() {
        super.onStart();

        executeSpiceRequest(searchRepositoriesRequest, CACHE_KEY, DurationInMillis.ONE_MINUTE);
    }



}

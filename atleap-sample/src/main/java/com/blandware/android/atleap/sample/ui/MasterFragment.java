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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.blandware.android.atleap.loader.LoaderManagerCreator;
import com.blandware.android.atleap.loader.SimpleCursorAdapterLoadable;
import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.sample.network.robospice.SearchRepositoriesRequest;
import com.blandware.android.atleap.sample.provider.DefaultContract;
import com.blandware.android.atleap.util.NavUtil;
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


        SimpleCursorAdapterLoadable adapter = new SimpleCursorAdapterLoadable(
                getActivity(),
                DefaultContract.CONTENT_URI_REPOSITORIES_USERS,
                new String[] {DefaultContract.Repository.TABLE+"."+DefaultContract.Repository._ID+" AS "+DefaultContract.Repository._ID, DefaultContract.User.AVATAR_URL, DefaultContract.Repository.FULL_NAME, DefaultContract.Repository.STARGAZERS_COUNT, DefaultContract.Repository.DESCRIPTION},
                null, //selection
                null, //selectionArgs
                DefaultContract.Repository.STARGAZERS_COUNT + " DESC", //sortOrder
                R.layout.listitem_repository,
                new String[] {DefaultContract.User.AVATAR_URL, DefaultContract.Repository.FULL_NAME, DefaultContract.Repository.STARGAZERS_COUNT, DefaultContract.Repository.DESCRIPTION},
                new int[] {R.id.avatar_url, R.id.full_name, R.id.stargazers_count, R.id.description}
        );
        LoaderManagerCreator loaderManagerCreator = new LoaderManagerCreator(this, adapter);

        ListView listView = (ListView)getView().findViewById(R.id.list_repositories);
        listView.setAdapter(adapter);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (view.getId() == R.id.avatar_url) {
                    ImageView imageView = (ImageView) view;
                    String avatarUrl = cursor.getString(i);
                    Picasso.with(getActivity()).load(avatarUrl).into(imageView);
                    return true;
                } else {
                    return false;
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                int repositoryId = cursor.getInt(cursor.getColumnIndex(DefaultContract.Repository._ID));
                NavUtil.replaceFragment(getActivity(), R.id.container, DetailFragment.newInstance(repositoryId));
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

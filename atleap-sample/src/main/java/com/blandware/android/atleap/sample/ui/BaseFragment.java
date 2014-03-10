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


import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.sample.service.NetworkService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by agrebnev on 22.12.13.
 */
public class BaseFragment<T> extends Fragment implements RequestListener<T> {

    private SpiceManager spiceManager = new SpiceManager(NetworkService.class);

    @Override
    public void onStart() {
        super.onStart();
        changeProgressBarVisibility(false);
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

    protected void executeSpiceRequest(SpiceRequest<T> request, Object requestCacheKey, long cacheExpiryDuration) {
        changeProgressBarVisibility(true);
        getSpiceManager().execute(request, requestCacheKey, cacheExpiryDuration, this);
    }

    @Override
    public void onRequestFailure(SpiceException e) {
        changeProgressBarVisibility(false);
        if (e instanceof NoNetworkException) {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestSuccess(Object o) {
        changeProgressBarVisibility(false);
    }

    protected void changeProgressBarVisibility(boolean show) {
        final ActionBarActivity actionBarActivity = (ActionBarActivity)getActivity();
        if (actionBarActivity != null) {
            actionBarActivity.setSupportProgressBarIndeterminateVisibility(show);
        }
    }


}

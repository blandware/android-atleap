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

package com.blandware.android.atleap.test.stub;

import android.os.Looper;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by agrebnev on 01.01.14.
 */
public class RequestListenerStub<T> implements RequestListener<T> {

    protected Boolean isSuccessful = null;
    protected boolean isExecutedInUIThread = false;

    protected ReentrantLock lock = new ReentrantLock();
    protected Condition requestFinishedCondition = lock.newCondition();
    protected Exception exception;
    protected List<T> resultHistory = new ArrayList<T>();

    @Override
    public void onRequestFailure(SpiceException exception) {
        lock.lock();
        try {
            checkIsExectuedInUIThread();
            isSuccessful = false;
            this.exception = exception;
            requestFinishedCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onRequestSuccess(T arg0) {
        lock.lock();
        try {
            checkIsExectuedInUIThread();
            isSuccessful = true;
            resultHistory.add(arg0);
            requestFinishedCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public List<T> getResultHistory() {
        return resultHistory;
    }

    protected void checkIsExectuedInUIThread() {
        if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper()) {
            isExecutedInUIThread = true;
        }
    }

    public Boolean isSuccessful() {
        return isSuccessful;
    }

    public Exception getReceivedException() {
        return exception;
    }

    public boolean isExecutedInUIThread() {
        return isExecutedInUIThread;
    }

    public void await(long millisecond) throws InterruptedException {
        lock.lock();
        try {
            if (isSuccessful != null) {
                return;
            }
            requestFinishedCondition.await(millisecond, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    public void resetSuccess() {
        this.isSuccessful = null;
    }
}

package com.blandware.android.atleap.sample.network.robospice;

import android.util.Log;

import com.blandware.android.atleap.sample.BuildConfig;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

import roboguice.util.temp.Ln;

/**
 * Created by agrebnev on 26.03.14.
 */
public class DefaultSpiceManager extends SpiceManager {
    public DefaultSpiceManager(Class<? extends SpiceService> spiceServiceClass) {
        super(spiceServiceClass);
        if(BuildConfig.DEBUG) {
            Ln.getConfig().setLoggingLevel(Log.VERBOSE);
        } else {
            Ln.getConfig().setLoggingLevel(Log.ERROR);
        }
    }
}

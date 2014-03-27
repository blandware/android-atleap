package com.blandware.android.atleap.sample.network.robospice;

import com.blandware.android.atleap.sample.exception.ServerErrorException;
import com.blandware.android.atleap.sample.exception.UnauthorizedException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Created by agrebnev on 26.03.14.
 */
public class NetworkRetryPolicy implements RetryPolicy {

    /** The default number of retry attempts. */
    public static final int DEFAULT_RETRY_COUNT = 3;

    /** The default delay before retry a request (in ms). */
    public static final long DEFAULT_DELAY_BEFORE_RETRY = 2500;

    /** The default backoff multiplier. */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /** The number of retry attempts. */
    private int retryCount = DEFAULT_RETRY_COUNT;

    /**
     * The delay to wait before next retry attempt. Will be multiplied by
     * {@link #backOffMultiplier} between every retry attempt.
     */
    private long delayBeforeRetry = DEFAULT_DELAY_BEFORE_RETRY;

    /**
     * The backoff multiplier. Will be multiplied by {@link #delayBeforeRetry}
     * between every retry attempt.
     */
    private float backOffMultiplier = DEFAULT_BACKOFF_MULT;

    public NetworkRetryPolicy(int retryCount, long delayBeforeRetry, float backOffMultiplier) {
        this.retryCount = retryCount;
        this.delayBeforeRetry = delayBeforeRetry;
        this.backOffMultiplier = backOffMultiplier;
    }

    public NetworkRetryPolicy() {
        this(DEFAULT_RETRY_COUNT, DEFAULT_DELAY_BEFORE_RETRY, DEFAULT_BACKOFF_MULT);
    }

    @Override
    public void retry(SpiceException e) {
        if (ExceptionUtils.indexOfType(e, ServerErrorException.class) >= 0) {
            //in case of 200 response and serverError like incorrect params
            //do not retry server errors
            retryCount = 0;
        } else if (ExceptionUtils.indexOfType(e, UnauthorizedException.class) >= 0) {
            //Special case for unauthorized error. Retry only one time.
            if (retryCount > 1)
                retryCount = 1;
            else
                retryCount--;

            backOffMultiplier = 0;
        } else {
            //default behaviour in case of NoNetwork error or server response with code >= 300
            retryCount--;
        }

        delayBeforeRetry = (long) (delayBeforeRetry * backOffMultiplier);
    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public long getDelayBeforeRetry() {
        return delayBeforeRetry;
    }
}
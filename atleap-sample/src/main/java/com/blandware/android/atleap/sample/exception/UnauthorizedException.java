package com.blandware.android.atleap.sample.exception;

/**
 * Created by agrebnev on 26.03.14.
 */
public class UnauthorizedException extends Exception {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String detailMessage) {
        super(detailMessage);
    }

    public UnauthorizedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnauthorizedException(Throwable throwable) {
        super(throwable);
    }
}

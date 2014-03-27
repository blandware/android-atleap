package com.blandware.android.atleap.sample.exception;

/**
 * Created by agrebnev on 26.03.14.
 */
public class DeveloperErrorException extends ServerErrorException {

    public DeveloperErrorException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }
}

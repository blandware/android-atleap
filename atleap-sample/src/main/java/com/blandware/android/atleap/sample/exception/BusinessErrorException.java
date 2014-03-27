package com.blandware.android.atleap.sample.exception;

/**
 * Created by agrebnev on 26.03.14.
 */
public class BusinessErrorException extends ServerErrorException {

    public BusinessErrorException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }
}

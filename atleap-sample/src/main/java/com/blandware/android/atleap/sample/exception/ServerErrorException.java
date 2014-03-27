package com.blandware.android.atleap.sample.exception;

/**
 * Created by agrebnev on 26.03.14.
 */
public class ServerErrorException extends Exception {

    private String errorCode;

    private String errorDetails;

    public ServerErrorException() {
        super();
    }

    public ServerErrorException(String errorCode, String errorDetails) {
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

}

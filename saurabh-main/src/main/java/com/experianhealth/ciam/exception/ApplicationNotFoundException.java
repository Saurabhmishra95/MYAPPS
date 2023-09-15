package com.experianhealth.ciam.exception;

import org.apache.http.HttpStatus;

public class ApplicationNotFoundException extends CIAMRuntimeException {
    public static final String PUBLIC_DETAIL_MESSAGE = "No application details found for the user. ";

    public ApplicationNotFoundException(String applicationId, String exceptionMessage) {
        super(exceptionMessage, PUBLIC_DETAIL_MESSAGE + applicationId, HttpStatus.SC_NOT_FOUND);
    }
    
    public ApplicationNotFoundException( String exceptionMessage) {
        super(exceptionMessage);
    }
}

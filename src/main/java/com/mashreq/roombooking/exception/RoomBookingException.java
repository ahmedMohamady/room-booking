package com.mashreq.roombooking.exception;

public class RoomBookingException extends RuntimeException {

    private final ErrorDetails errorDetails;


    public RoomBookingException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }
}

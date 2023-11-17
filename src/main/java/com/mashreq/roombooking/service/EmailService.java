package com.mashreq.roombooking.service;

import com.mashreq.roombooking.dto.BookingDTO;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendBookingEmail(BookingDTO booking) throws MessagingException;
}

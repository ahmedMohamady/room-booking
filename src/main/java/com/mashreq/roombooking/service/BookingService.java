package com.mashreq.roombooking.service;

import com.mashreq.roombooking.dto.BookingDTO;
import jakarta.mail.MessagingException;

import java.time.LocalTime;
import java.util.List;

public interface BookingService {
    BookingDTO bookRoom(BookingDTO bookingDTO) throws MessagingException;

    List<BookingDTO> getBookingsWithRange(LocalTime startTime, LocalTime endTime);

}

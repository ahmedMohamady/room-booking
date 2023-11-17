package com.mashreq.roombooking.repository;

import com.mashreq.roombooking.entity.Booking;
import com.mashreq.roombooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingByStartTimeBetween(LocalDateTime to, LocalDateTime from);
}

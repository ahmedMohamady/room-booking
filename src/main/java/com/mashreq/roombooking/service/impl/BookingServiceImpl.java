package com.mashreq.roombooking.service.impl;

import com.mashreq.roombooking.dto.BookingDTO;
import com.mashreq.roombooking.dto.RoomDTO;
import com.mashreq.roombooking.entity.Booking;
import com.mashreq.roombooking.entity.MaintenanceTiming;
import com.mashreq.roombooking.exception.ErrorDetails;
import com.mashreq.roombooking.exception.RoomBookingException;
import com.mashreq.roombooking.repository.BookingRepository;
import com.mashreq.roombooking.repository.MaintenanceTimingRepository;
import com.mashreq.roombooking.repository.RoomRepository;
import com.mashreq.roombooking.service.BookingService;
import com.mashreq.roombooking.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
public class BookingServiceImpl implements BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final MaintenanceTimingRepository maintenanceTimingRepository;

    private final EmailService emailService;


    public BookingServiceImpl(RoomRepository roomRepository, BookingRepository bookingRepository, MaintenanceTimingRepository maintenanceTimingRepository, EmailService emailService) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.maintenanceTimingRepository = maintenanceTimingRepository;
        this.emailService = emailService;
    }


    @Override
    public BookingDTO bookRoom(BookingDTO bookingDTO) throws MessagingException {
        var today = LocalDate.now();
        checkBookingOverlapMaintenanceTime(bookingDTO.getStartTime(), bookingDTO.getEndTime());
        var rooms = roomRepository.findAvailableRooms(bookingDTO.getStartTime().atDate(today), bookingDTO.getEndTime().atDate(today), bookingDTO.getNoOfPeople());
        var room = rooms.stream().findFirst();
        if (room.isPresent()) {
            var bookingEntity = new Booking();
            bookingEntity.setStartTime(bookingDTO.getStartTime().atDate(today));
            bookingEntity.setEndTime(bookingDTO.getEndTime().atDate(today));
            bookingEntity.setRoom(room.get());
            bookingRepository.save(bookingEntity);
            bookingDTO.setRoom(new RoomDTO(room.get().getConferenceName(), room.get().getCapacity()));
            if (bookingDTO.getEmail() != null) emailService.sendBookingEmail(bookingDTO);
            return bookingDTO;
        } else {
            throw new RoomBookingException(new ErrorDetails(HttpStatus.CONFLICT, "No room available at given time"));
        }
    }

    @Override
    public List<BookingDTO> getBookingsWithRange(LocalTime startTime, LocalTime endTime) {
        var today = LocalDate.now();
        return bookingRepository.findBookingByStartTimeBetween(startTime.atDate(today), endTime.atDate(today)).stream().map(booking -> {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setStartTime(booking.getStartTime().toLocalTime());
            bookingDTO.setEndTime(booking.getEndTime().toLocalTime());
            bookingDTO.setEmail(booking.getEmail());
            bookingDTO.setNotes(booking.getNotes());
            bookingDTO.setRoom(new RoomDTO(booking.getRoom().getConferenceName(), booking.getRoom().getCapacity()));
            return bookingDTO;
        }).toList();
    }


    private void checkBookingOverlapMaintenanceTime(LocalTime startDate, LocalTime endDate) {
        List<MaintenanceTiming> maintenanceTimings = maintenanceTimingRepository.findAll();
        maintenanceTimings.forEach(maintenanceTiming -> {
            if (maintenanceTiming.getEndTime().isAfter(startDate) && maintenanceTiming.getStartTime().isBefore(endDate)) {
                throw new RoomBookingException(new ErrorDetails(HttpStatus.NOT_ACCEPTABLE, "given time is overlapped with maintenance time"));
            }
        });
    }

}

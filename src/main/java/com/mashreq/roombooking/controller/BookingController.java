package com.mashreq.roombooking.controller;


import com.mashreq.roombooking.dto.BookingDTO;
import com.mashreq.roombooking.exception.ErrorDetails;
import com.mashreq.roombooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/booking")
@Tag(name = "BookingApi", description = "Booking Rest Api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @Operation(summary = "Book Room", description = "Book Room End-Point", tags = {
            "BookingApi"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room Booked", content = @Content(schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "409", description = "No Room Available", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "406", description = "Overlapped with maintenance time", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
    @PostMapping("/book-room")
    public ResponseEntity<BookingDTO> bookRoom(@RequestBody @Valid BookingDTO bookingDTO) throws MessagingException {
        return ResponseEntity.ok(bookingService.bookRoom(bookingDTO));
    }

    @Operation(summary = "Retrieve Bookings ", description = "Retrieve Bookings by date range End-Point", tags = {
            "BookingApi"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = List.class)))})
    @GetMapping
    public ResponseEntity<List<BookingDTO>> retrieveBookingsWithRange(@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                                       @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endTime) {
        return ResponseEntity.ok(bookingService.getBookingsWithRange(startTime,endTime));
    }
}

package com.mashreq.roombooking.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashreq.roombooking.dto.BookingDTO;
import com.mashreq.roombooking.entity.Booking;
import com.mashreq.roombooking.exception.ErrorDetails;
import com.mashreq.roombooking.repository.BookingRepository;
import com.mashreq.roombooking.repository.RoomRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;


    @Autowired
    private RoomRepository roomRepository;


    @BeforeEach
    void setup() {
        bookingRepository.deleteAll();
    }


    @AfterAll
    void clear() {
        bookingRepository.deleteAll();
    }


    @Test
    void bookRoom_whenAllRoomsIsAvailable_thenStatus200()
            throws Exception {

        var bookingDto = new BookingDTO();
        bookingDto.setStartTime(LocalTime.of(11, 0));
        bookingDto.setEndTime(LocalTime.of(12, 0));
        bookingDto.setNoOfPeople(2);
        var result = mvc.perform(MockMvcRequestBuilders.post("/booking/book-room")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), BookingDTO.class);
        assertEquals(3, response.getRoom().getCapacity());
        assertEquals("Amaze", response.getRoom().getConferenceName());
    }


    @Test
    void bookRoom_whenNearestRoomCapIsAvailable_thenStatus200()
            throws Exception {

        bookRoomWith3Capacity();
        var bookingDto = new BookingDTO();
        bookingDto.setStartTime(LocalTime.of(11, 0));
        bookingDto.setEndTime(LocalTime.of(12, 0));
        bookingDto.setNoOfPeople(3);
        var result = mvc.perform(MockMvcRequestBuilders.post("/booking/book-room")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), BookingDTO.class);
        assertEquals(7, response.getRoom().getCapacity());
        assertEquals("Beauty", response.getRoom().getConferenceName());
    }



    @Test
    void bookRoom_whenAllRoomsIsNotAvailable_thenStatus400()
            throws Exception {
        bookAllRooms();
        var bookingDto = new BookingDTO();
        bookingDto.setStartTime(LocalTime.of(12, 0));
        bookingDto.setEndTime(LocalTime.of(13, 0));
        bookingDto.setNoOfPeople(3);
        var result = mvc.perform(MockMvcRequestBuilders.post("/booking/book-room")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals("No room available at given time", response.getMessage());
    }


    @Test
    void bookRoom_whenMaintenanceTime_thenStatus400()
            throws Exception {
        var bookingDto = new BookingDTO();
        bookingDto.setStartTime(LocalTime.of(9, 0));
        bookingDto.setEndTime(LocalTime.of(9, 30));
        bookingDto.setNoOfPeople(10);
        var result = mvc.perform(MockMvcRequestBuilders.post("/booking/book-room")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals("given time is overlapped with maintenance time", response.getMessage());
    }


    @Test
    void getBooking_whenNoBooking_thenStatus200()
            throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.get("/booking")
                        .contentType(MediaType.APPLICATION_JSON).queryParam("startTime","10:00").queryParam("endTime","12:00"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<BookingDTO>>() {
        });
        assertEquals(0, response.size());
    }



    private void bookRoomWith3Capacity() {
        var booking = new Booking();
        booking.setStartTime(LocalTime.of(11, 0).atDate(LocalDate.now()));
        booking.setEndTime(LocalTime.of(12, 0).atDate(LocalDate.now()));
        booking.setRoom(roomRepository.getReferenceById(1L));
        bookingRepository.save(booking);
    }


    private void bookAllRooms() {
        AtomicReference<Booking> booking = new AtomicReference<>();
        LongStream.range(1,5).forEach(id -> {
            booking.set(new Booking());
            booking.get().setStartTime(LocalTime.of(12, 30).atDate(LocalDate.now()));
            booking.get().setEndTime(LocalTime.of(13, 0).atDate(LocalDate.now()));
            booking.get().setRoom(roomRepository.getReferenceById(id));
            bookingRepository.save(booking.get());
        });
    }


}

package com.mashreq.roombooking.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashreq.roombooking.dto.RoomDTO;
import com.mashreq.roombooking.entity.Booking;
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
class RoomControllerIT {

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
    void availableRooms_whenAllRoomsIsAvailable_thenStatus200()
            throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.get("/room/available")
                        .contentType(MediaType.APPLICATION_JSON).queryParam("startTime","10:00").queryParam("endTime","12:00"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<RoomDTO>>() {
        });
        assertEquals(4, response.size());
    }


    @Test
    void availableRooms_whenAllRoomsIsBooked_thenStatus200()
            throws Exception {
        bookAllRooms();
        var result = mvc.perform(MockMvcRequestBuilders.get("/room/available")
                        .contentType(MediaType.APPLICATION_JSON).queryParam("startTime","11:00").queryParam("startTime","13:00"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<RoomDTO>>() {
        });
        assertEquals(0, response.size());
    }



    private void bookAllRooms() {
        AtomicReference<Booking> booking = new AtomicReference<>();
        LongStream.range(1, 5).forEach(id -> {
            booking.set(new Booking());
            booking.get().setStartTime(LocalTime.of(12, 30).atDate(LocalDate.now()));
            booking.get().setEndTime(LocalTime.of(13, 0).atDate(LocalDate.now()));
            booking.get().setRoom(roomRepository.getReferenceById(id));
            bookingRepository.save(booking.get());
        });
    }


}

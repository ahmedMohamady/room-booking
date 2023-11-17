package com.mashreq.roombooking.service.impl;

import com.mashreq.roombooking.dto.RoomDTO;
import com.mashreq.roombooking.repository.RoomRepository;
import com.mashreq.roombooking.service.RoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomDTO> getAvailableRooms(LocalTime startTime, LocalTime endTime) {
        var today = LocalDate.now();
        return roomRepository.findAvailableRooms(startTime.atDate(today), endTime.atDate(today), 0).stream().map(entity ->
                new RoomDTO(entity.getConferenceName(), entity.getCapacity())).toList();
    }
}

package com.mashreq.roombooking.service;

import com.mashreq.roombooking.dto.RoomDTO;

import java.time.LocalTime;
import java.util.List;

public interface RoomService {
    List<RoomDTO> getAvailableRooms(LocalTime startTime, LocalTime endTime);
}

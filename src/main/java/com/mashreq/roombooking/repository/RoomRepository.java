package com.mashreq.roombooking.repository;

import com.mashreq.roombooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("FROM Room r LEFT JOIN Booking b ON b.room.id = r.id AND b.startTime < ?2 AND b.endTime > ?1 " +
            "WHERE r.capacity >= ?3 AND b.room IS NULL ORDER BY r.capacity ASC")
    List<Room> findAvailableRooms(LocalDateTime startTime, LocalDateTime endTime, Integer capacity);
}

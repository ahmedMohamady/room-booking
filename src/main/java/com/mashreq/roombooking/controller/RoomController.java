package com.mashreq.roombooking.controller;


import com.mashreq.roombooking.dto.RoomDTO;
import com.mashreq.roombooking.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/room")
@Tag(name = "RoomApi", description = "Room Rest Api")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    @Operation(summary = "Retrieve available Room", description = "Retrieve available Room End-Point", tags = {
            "RoomApi"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = List.class)))})
    @GetMapping("/available")
    public ResponseEntity<List<RoomDTO>> retrieveAvailableRooms(@RequestParam @DateTimeFormat(pattern = "HH:MM") LocalTime startTime,
                                                                @RequestParam @DateTimeFormat(pattern = "HH:MM") LocalTime endTime) {
        return ResponseEntity.ok(roomService.getAvailableRooms(startTime, endTime));
    }
}

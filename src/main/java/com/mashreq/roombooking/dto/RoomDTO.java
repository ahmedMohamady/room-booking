package com.mashreq.roombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RoomDTO {
    private String conferenceName;
    private Integer capacity;
}

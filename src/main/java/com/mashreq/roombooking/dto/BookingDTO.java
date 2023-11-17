package com.mashreq.roombooking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;


@Data
public class BookingDTO {
    @NotNull
    @Min(value = 2, message = "noOfPeople should be greater than 1")
    @Max(value = 20, message = "noOfPeople should be less than or equal 20")
    private Integer noOfPeople;
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", pattern = "HH:mm", example = "12:00")
    private LocalTime startTime;
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", pattern = "HH:mm", example = "13:00")
    private LocalTime endTime;
    private String email;
    private String notes;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private RoomDTO room;
}

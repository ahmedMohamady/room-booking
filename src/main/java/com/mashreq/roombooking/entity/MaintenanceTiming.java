package com.mashreq.roombooking.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Entity
@Table(name = "maintenance_timing")
@Getter
@Setter
@ToString
public class MaintenanceTiming {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_time")
    private LocalTime startTime;
    @NotNull
    @Column(name = "end_time")
    private LocalTime endTime;
}

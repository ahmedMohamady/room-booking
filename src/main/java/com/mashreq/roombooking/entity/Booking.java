package com.mashreq.roombooking.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "email")
    private String email;

    @Column(name = "notes")
    private String notes;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}

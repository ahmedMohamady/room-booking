package com.mashreq.roombooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "room")
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "conference_name")
    private String conferenceName ;
    @Column(name = "capacity")
    private Integer capacity;
}

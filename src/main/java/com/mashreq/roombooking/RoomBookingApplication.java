package com.mashreq.roombooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RoomBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomBookingApplication.class, args);
    }

}

package com.ikucuk.Restaurant.Reservation.Website.dto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDto {

    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numOfAdults;
    private int numOfChildren;
    private int totalOfGuest;
    private String bookingConfirmationCode;
    private UserDto user;
    private DeskDto desk;
}

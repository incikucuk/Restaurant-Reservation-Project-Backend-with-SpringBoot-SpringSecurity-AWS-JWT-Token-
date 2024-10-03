package com.ikucuk.Restaurant.Reservation.Website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ikucuk.Restaurant.Reservation.Website.entity.Booking;
import com.ikucuk.Restaurant.Reservation.Website.entity.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;

    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;

    private UserDto user;
    private DeskDto desk;
    private BookingDto booking;
    private List<UserDto> userList;
    private List<BookingDto> bookingList;
    private List<DeskDto> deskList;

}

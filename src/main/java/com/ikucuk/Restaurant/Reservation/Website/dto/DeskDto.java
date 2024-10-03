package com.ikucuk.Restaurant.Reservation.Website.dto;

import com.ikucuk.Restaurant.Reservation.Website.entity.Booking;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeskDto {
    private Long id;
    private String deskType;
    private BigDecimal deskPrice;
    private String deskPhotoUrl;
    private String description;
    private List<BookingDto> bookingList = new ArrayList<>();

}

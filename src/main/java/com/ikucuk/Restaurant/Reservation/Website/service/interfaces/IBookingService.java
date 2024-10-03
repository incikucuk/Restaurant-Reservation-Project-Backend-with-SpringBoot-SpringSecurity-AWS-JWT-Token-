package com.ikucuk.Restaurant.Reservation.Website.service.interfaces;

import com.ikucuk.Restaurant.Reservation.Website.dto.Response;
import com.ikucuk.Restaurant.Reservation.Website.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long bookingId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookId);
}

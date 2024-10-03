package com.ikucuk.Restaurant.Reservation.Website.reporsitory;

import com.ikucuk.Restaurant.Reservation.Website.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

}

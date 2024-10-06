package com.ikucuk.Restaurant.Reservation.Website.controller;

import com.ikucuk.Restaurant.Reservation.Website.dto.Response;
import com.ikucuk.Restaurant.Reservation.Website.entity.Booking;
import com.ikucuk.Restaurant.Reservation.Website.service.interfaces.IBookingService;
import com.ikucuk.Restaurant.Reservation.Website.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @PostMapping("/book-desk/{deskId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBooking(
            @PathVariable Long deskId,
            @PathVariable Long userId,
            @RequestBody Booking bookingRequest){
        Response response = bookingService.saveBooking(deskId, userId, bookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingsByConfirmationCode(@PathVariable String confirmationCode) {
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteDeskBooking(@PathVariable Long deskId) {
        Response response = bookingService.cancelBooking(deskId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

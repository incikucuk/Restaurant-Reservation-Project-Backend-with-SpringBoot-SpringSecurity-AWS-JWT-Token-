package com.ikucuk.Restaurant.Reservation.Website.service.impl;

import com.ikucuk.Restaurant.Reservation.Website.dto.BookingDto;
import com.ikucuk.Restaurant.Reservation.Website.dto.DeskDto;
import com.ikucuk.Restaurant.Reservation.Website.dto.Response;
import com.ikucuk.Restaurant.Reservation.Website.entity.Booking;
import com.ikucuk.Restaurant.Reservation.Website.entity.Desk;
import com.ikucuk.Restaurant.Reservation.Website.entity.User;
import com.ikucuk.Restaurant.Reservation.Website.exception.OurException;
import com.ikucuk.Restaurant.Reservation.Website.reporsitory.BookingRepository;
import com.ikucuk.Restaurant.Reservation.Website.reporsitory.DeskRepository;
import com.ikucuk.Restaurant.Reservation.Website.reporsitory.UserRepository;
import com.ikucuk.Restaurant.Reservation.Website.service.interfaces.IBookingService;
import com.ikucuk.Restaurant.Reservation.Website.service.interfaces.IDeskService;
import com.ikucuk.Restaurant.Reservation.Website.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements IBookingService {

   @Autowired
   private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private IDeskService deskService;


    @Override
    public Response saveBooking(Long deskId, Long userId, Booking bookingRequest) {
        Response response = new Response();
        try{
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new IllegalArgumentException("Check in date must come before check out date");
            }
            Desk desk = deskRepository.findById(deskId).orElseThrow(()->new OurException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User not found"));

            List<Booking> existingBooking = desk.getBookingList();
            if(!deskIsAvailable(bookingRequest,existingBooking)){
                throw new OurException("Desk not available for the selected date range");
            }
            bookingRequest.setDesk(desk);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting available desks " + e.getMessage());
        }
        return response;
    }

    private boolean deskIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                        ||(bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                        || bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate())

                        ||bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())

                        ||bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                         && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())
                );
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try{
            Booking confCode = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new OurException("Confirmation code not exist"));
            BookingDto bookingDto = Utils.mapBookingEntityToBookingDtoPlusBookedRooms(confCode,true);
            response.setBooking(bookingDto);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting confirmation code " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try{
            List<Booking> bookingList = bookingRepository.findAll();
            List<BookingDto> bookingDtoList = Utils.mapBookingListEntityToBookingListDto(bookingList);
            response.setBookingList(bookingDtoList);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try{
           bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking not exists!"));
           bookingRepository.deleteById(bookingId);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error cancelling a bookings" + e.getMessage());
        }
        return response;
    }
}

package com.ikucuk.Restaurant.Reservation.Website.utils;

import com.ikucuk.Restaurant.Reservation.Website.dto.BookingDto;
import com.ikucuk.Restaurant.Reservation.Website.dto.DeskDto;
import com.ikucuk.Restaurant.Reservation.Website.dto.UserDto;
import com.ikucuk.Restaurant.Reservation.Website.entity.Booking;
import com.ikucuk.Restaurant.Reservation.Website.entity.Desk;
import com.ikucuk.Restaurant.Reservation.Website.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGIJKLMNOQRSTUVWYZ0123456789";

    private static final SecureRandom secureRandom = new SecureRandom();

    public static  String generateRandomConfirmationCode(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<length;i++){
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDto mapUserEntityToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static DeskDto mapDeskEntityToDeskDto(Desk desk){
        DeskDto deskDto = new DeskDto();
        deskDto.setId(desk.getId());
        deskDto.setDeskType(desk.getDeskType());
        deskDto.setDeskPrice(desk.getDeskPrice());
        deskDto.setDeskPhotoUrl(desk.getDeskPhotoUrl());
        deskDto.setDescription(desk.getDescription());

        if(desk.getBookingList() != null){
            deskDto.setBookingList(desk.getBookingList().stream().map(Utils::mapBookingEntityToBookingDto).
                    collect(Collectors.toList()));
        }

        return deskDto;
    }

    public static BookingDto mapBookingEntityToBookingDto(Booking booking){
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumOfChildren(booking.getNumOfChildren());
        bookingDto.setNumOfAdults(booking.getNumOfAdults());
        bookingDto.setTotalOfGuest(booking.getTotalOfGuest());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        return bookingDto;
    }

    public static BookingDto mapBookingEntityToBookingDtoPlusBookedRooms(Booking booking, boolean mapUser){
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumOfChildren(booking.getNumOfChildren());
        bookingDto.setNumOfAdults(booking.getNumOfAdults());
        bookingDto.setTotalOfGuest(booking.getTotalOfGuest());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if(mapUser){
            bookingDto.setUser(Utils.mapUserEntityToUserDto(booking.getUser()));
        }

        if(booking.getDesk() != null){
            DeskDto deskDto = new DeskDto();

            deskDto.setId(booking.getDesk().getId());
            deskDto.setDeskType(booking.getDesk().getDeskType());
            deskDto.setDeskPrice(booking.getDesk().getDeskPrice());
            deskDto.setDeskPhotoUrl(booking.getDesk().getDeskPhotoUrl());
            deskDto.setDescription(booking.getDesk().getDescription());
            bookingDto.setDesk(deskDto);
        }
        return bookingDto;
    }

    public static UserDto mapUserEntityToUserDtoPlusUserBookingsAndRoom(User user){
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

        if(!user.getBookings().isEmpty()){
            userDto.setBookings(user.getBookings().stream().map(
                    booking -> mapBookingEntityToBookingDtoPlusBookedRooms(booking,false))
                    .collect(Collectors.toList()));
        }
    return userDto;
    }

    public static List<UserDto> mapUserListEntityToUserListDto(List<User> userList){
        return userList.stream().map(Utils::mapUserEntityToUserDto).collect(Collectors.toList());
    }
    public static List<DeskDto> mapDeskListEntityToDeskListDto(List<Desk> deskList){
        return deskList.stream().map(Utils::mapDeskEntityToDeskDto).collect(Collectors.toList());
    }
    public static List<BookingDto> mapBookingListEntityToBookingListDto(List<Booking> bookingList){
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDto).collect(Collectors.toList());
    }
}

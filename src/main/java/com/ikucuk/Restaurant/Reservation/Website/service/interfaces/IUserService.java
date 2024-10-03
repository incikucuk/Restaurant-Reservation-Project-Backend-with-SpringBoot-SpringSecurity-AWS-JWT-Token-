package com.ikucuk.Restaurant.Reservation.Website.service.interfaces;

import com.ikucuk.Restaurant.Reservation.Website.dto.LoginRequest;
import com.ikucuk.Restaurant.Reservation.Website.dto.Response;
import com.ikucuk.Restaurant.Reservation.Website.entity.User;

public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);


}

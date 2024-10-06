package com.ikucuk.Restaurant.Reservation.Website.service.impl;

import com.ikucuk.Restaurant.Reservation.Website.dto.LoginRequest;
import com.ikucuk.Restaurant.Reservation.Website.dto.Response;
import com.ikucuk.Restaurant.Reservation.Website.dto.UserDto;
import com.ikucuk.Restaurant.Reservation.Website.entity.User;
import com.ikucuk.Restaurant.Reservation.Website.exception.OurException;
import com.ikucuk.Restaurant.Reservation.Website.reporsitory.UserRepository;
import com.ikucuk.Restaurant.Reservation.Website.service.interfaces.IUserService;
import com.ikucuk.Restaurant.Reservation.Website.utils.JWTUtils;
import com.ikucuk.Restaurant.Reservation.Website.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {
        Response response = new Response();
        try{
            if(user.getRole() == null || user.getRole().isBlank()){  //postmanda userRole alanı girilmeze USER olarak atanir
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail() + "already exists!");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDto userDto = Utils.mapUserEntityToUserDto(savedUser);

            response.setStatusCode(200);
            response.setUser(userDto);

        }catch(OurException e){
            response.setStatusCode(400);
            response.setMessage("Error saving a user" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try{
            //1.adim kimlik dorulama yapilir
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

            //var tipinde dönen değere uyum saglamasi icin tanimlanir
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("User cannot found"));
            var token = jwtUtils.generateToken(user);

            response.setMessage("Sucessfully!");
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 days");
            response.setStatusCode(200);

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error logging a user" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try{
            List<User> userList = userRepository.findAll();
            List<UserDto> userDtoList = Utils.mapUserListEntityToUserListDto(userList);

            response.setUserList(userDtoList);
            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error logging a user" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();
        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
            UserDto userDto = Utils.mapUserEntityToUserDtoPlusUserBookingsAndRoom(user);

            response.setUser(userDto);
            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user bookings in" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();
        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
            userRepository.deleteById(Long.valueOf(userId));

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error logging a user" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();
        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
            UserDto userDto = Utils.mapUserEntityToUserDto(user);

            response.setUser(userDto);
            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting a user by id " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();
        try{
            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User couldn't found!"));
            UserDto userDto = Utils.mapUserEntityToUserDto(user);

            response.setUser(userDto);
            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting a user info  " + e.getMessage());
        }
        return response;
    }
}

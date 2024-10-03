package com.ikucuk.Restaurant.Reservation.Website.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;

public class TestToken {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //we should find the encode password who name of user's password in db and then we have to change these inputs with others in db as encode
        System.out.println(passwordEncoder.encode("test1234"));
       // System.out.println(passwordEncoder.encode("admin"));
    }

}

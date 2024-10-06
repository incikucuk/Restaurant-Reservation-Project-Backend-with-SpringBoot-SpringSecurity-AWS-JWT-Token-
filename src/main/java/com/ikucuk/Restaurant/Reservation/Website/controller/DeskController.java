package com.ikucuk.Restaurant.Reservation.Website.controller;

import com.ikucuk.Restaurant.Reservation.Website.dto.Response;
import com.ikucuk.Restaurant.Reservation.Website.entity.Desk;
import com.ikucuk.Restaurant.Reservation.Website.service.interfaces.IBookingService;
import com.ikucuk.Restaurant.Reservation.Website.service.interfaces.IDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/desks")
public class DeskController {

    @Autowired
    private IDeskService deskService;

    @Autowired
    private IBookingService bookingService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewDesk(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "deskType", required = false) String deskType,
            @RequestParam(value = "deskPrice", required = false) BigDecimal deskPrice,
            @RequestParam(value = "description", required = false) String description
            ){
        if(photo == null || photo.isEmpty() || deskType == null || deskType.isBlank() || deskPrice == null){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please Provide values for all fields(photo, roomType, roomPrice)");
        }
        Response response = deskService.addNewDesk(photo, deskType, deskPrice, description);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/desks/{deskId}")
    public ResponseEntity<Response> getDeskById(@PathVariable("deskId") Long deskId){
        Response response = deskService.getDeskById(deskId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllDesks(){
        Response response = deskService.getAllDesk();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public List<String> getDeskTypes(){
        return deskService.getAllDeskTypes();
    }

    @GetMapping("/all-available-desks")
    public ResponseEntity<Response> getAvailableDesks(){
        Response response = deskService.getAllAvailableDesks();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all-available-desks-by-date-and-type")
    public ResponseEntity<Response> getAvailableDesksByDateAndType(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkOutDate,
            @RequestParam(required = false) String deskType
            ){
        if(checkInDate == null || checkOutDate == null || deskType.isBlank()){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("All fields are required(checkInDate, checkOutDate, deskType)");
        }

        Response response = deskService.getAvailableDesksByDateAndType(checkInDate, checkOutDate, deskType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{deskId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateDesk(
            @PathVariable Long deskId,
            @RequestParam(value = "photo",required = false) MultipartFile photo,
            @RequestParam(value = "deskType",required = false) String deskType,
            @RequestParam(value = "deskPrice",required = false) BigDecimal deskPrice,
            @RequestParam(value = "deskDescription",required = false) String deskDescription){

        Response response = deskService.updateDesk(deskId, photo, deskType, deskPrice, deskDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{deskId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteDesk(@PathVariable("deskId") Long deskId){
        Response response = deskService.deleteDesk(deskId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

package com.ikucuk.Restaurant.Reservation.Website.service.impl;

import com.ikucuk.Restaurant.Reservation.Website.dto.DeskDto;
import com.ikucuk.Restaurant.Reservation.Website.dto.Response;
import com.ikucuk.Restaurant.Reservation.Website.dto.UserDto;
import com.ikucuk.Restaurant.Reservation.Website.entity.Desk;
import com.ikucuk.Restaurant.Reservation.Website.entity.User;
import com.ikucuk.Restaurant.Reservation.Website.exception.OurException;
import com.ikucuk.Restaurant.Reservation.Website.reporsitory.BookingRepository;
import com.ikucuk.Restaurant.Reservation.Website.reporsitory.DeskRepository;
import com.ikucuk.Restaurant.Reservation.Website.service.AwsS3Service;
import com.ikucuk.Restaurant.Reservation.Website.service.interfaces.IDeskService;
import com.ikucuk.Restaurant.Reservation.Website.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DeskServiceImpl implements IDeskService {

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Response addNewDesk(MultipartFile photo, String deskType, BigDecimal deskPrice, String description) {
        Response response = new Response();
        try{

            String imageUrl = awsS3Service.saveImageToS3(photo);
            Desk desk = new Desk();
            desk.setDeskPhotoUrl(imageUrl);
            desk.setDeskType(deskType);
            desk.setDeskPrice(deskPrice);
            desk.setDescription(description);

            Desk savedDesk = deskRepository.save(desk);
            DeskDto savedDeskDto = Utils.mapDeskEntityToDeskDto(savedDesk);
            response.setDesk(savedDeskDto);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting a desk " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllDeskTypes() {
        return deskRepository.findDistinctDeskTypes();
    }

    @Override
    public Response getAllDesk() {
        Response response = new Response();
        try{
            List<Desk> deskList = deskRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));  //azalan id siralama islemi
            List<DeskDto> deskDtoList = Utils.mapDeskListEntityToDeskListDto(deskList);

            response.setDeskList(deskDtoList);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all desks " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteDesk(Long deskId) {
        Response response = new Response();
        try{
           deskRepository.findById(deskId).orElseThrow(() -> new OurException("Desk not exist!"));
           deskRepository.deleteById(deskId);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all desks " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateDesk(Long deskId, MultipartFile photo, String deskType, BigDecimal deskPrice, String description) {
        Response response = new Response();
        try{
            String imageUrl = null;
            if(photo != null && !photo.isEmpty()){
                imageUrl = awsS3Service.saveImageToS3(photo);
            }

            Desk desk =  deskRepository.findById(deskId).orElseThrow(() -> new OurException("Desk not exist!"));

            if(deskType != null)desk.setDeskType(deskType);
            if(deskPrice != null)desk.setDeskPrice(deskPrice);
            if(description != null)desk.setDescription(description);

            Desk savedDesk = deskRepository.save(desk);
            DeskDto savedDeskDto = Utils.mapDeskEntityToDeskDto(savedDesk);
            response.setDesk(savedDeskDto);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting update " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getDeskById(Long deskId) {
        Response response = new Response();
        try{
            Desk desk = deskRepository.findById(deskId).orElseThrow(() -> new OurException("Desk not exist!"));
            DeskDto deskDto = Utils.mapDeskEntityToDeskDto(desk);
            response.setDesk(deskDto);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting desk by id " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableDesksByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String deskType) {
        Response response = new Response();
        try{
            List<Desk> desks = deskRepository.findByAvailableRoomsByDateAndTypes(checkInDate,checkOutDate,deskType);
            List<DeskDto> deskDtoList = Utils.mapDeskListEntityToDeskListDto(desks);
            response.setDeskList(deskDtoList);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting available desks " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableDesks() {
        Response response = new Response();
        try{
            List<Desk> desks = deskRepository.getAvailableDesks();
            List<DeskDto> deskDtoList = Utils.mapDeskListEntityToDeskListDto(desks);
            response.setDeskList(deskDtoList);

            response.setStatusCode(200);
            response.setMessage("Sucessful");

        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting available desks " + e.getMessage());
        }
        return response;
    }
}

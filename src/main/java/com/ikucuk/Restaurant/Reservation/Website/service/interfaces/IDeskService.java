package com.ikucuk.Restaurant.Reservation.Website.service.interfaces;

import com.ikucuk.Restaurant.Reservation.Website.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IDeskService {

    Response addNewDesk(MultipartFile photo, String deskType, BigDecimal deskPrice, String description);

    List<String> getAllDeskTypes();

    Response getAllDesk();

    Response deleteDesk(Long deskId);

    Response updateDesk(Long deskId, MultipartFile photo, String deskType, BigDecimal deskPrice, String description);

    Response getDeskById(Long deskId);

    Response getAvailableDesksByDateAndType(LocalDate checkInDate,LocalDate checkOutDate,String deskType);

    Response getAllAvailableDesks();


}

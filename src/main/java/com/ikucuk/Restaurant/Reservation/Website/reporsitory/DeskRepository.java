package com.ikucuk.Restaurant.Reservation.Website.reporsitory;

import com.ikucuk.Restaurant.Reservation.Website.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DeskRepository extends JpaRepository<Desk, Long> {

    @Query("SELECT DISTINCT r.deskType FROM Desk r")
    List<String> findDistinctDeskTypes();

    @Query("SELECT r FROM Desk r WHERE r.id NOT IN (SELECT b.desk.id FROM Booking b)")
    List<Desk> getAvailableDesks();

    @Query("SELECT r FROM Desk r WHERE r.deskType LIKE %:deskType% AND r.id NOT IN (SELECT bk.desk.id FROM Booking bk WHERE"+
    "(bk.checkInDate <= :checkOutDate) AND (bk.checkOutDate >= :checkInDate))")
    List<Desk> findByAvailableRoomsByDateAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String deskType);


}

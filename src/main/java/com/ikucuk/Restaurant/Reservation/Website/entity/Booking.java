package com.ikucuk.Restaurant.Reservation.Website.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Check in date is required")
    private LocalDate checkInDate;

    @Future(message = "Check out date must be in the future")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "Check numOfAdults is required")
    private int numOfAdults;

    @Min(value = 0, message = "Check numOfChildren is required")
    private int numOfChildren;

    private int totalOfGuest;

    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desk_id")
    private Desk desk;

    public void calculateTotalNumberOfQuests(){
        this.totalOfGuest = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfQuests();
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfQuests();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                ", totalOfGuest=" + totalOfGuest +
                ", numOfChildren=" + numOfChildren +
                ", numOfAdults=" + numOfAdults +
                ", checkOutDate=" + checkOutDate +
                ", checkInDate=" + checkInDate +
                ", id=" + id +
                '}';
    }
}

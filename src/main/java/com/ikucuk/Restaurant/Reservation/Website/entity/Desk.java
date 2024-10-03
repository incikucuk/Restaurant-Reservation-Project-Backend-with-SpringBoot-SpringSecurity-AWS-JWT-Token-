package com.ikucuk.Restaurant.Reservation.Website.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "desks")
public class Desk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deskType;
    private BigDecimal deskPrice;
    private String deskPhotoUrl;
    private String description;

    @OneToMany(mappedBy = "desk", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Booking> bookingList = new ArrayList<>();

    @Override
    public String toString() {
        return "Desk{" +
                "id=" + id +
                ", deskType='" + deskType + '\'' +
                ", deskPrice=" + deskPrice +
                ", deskPhotoUrl='" + deskPhotoUrl + '\'' +
                ", description='" + description + '\'' +
                ", bookingList=" + bookingList +
                '}';
    }
}

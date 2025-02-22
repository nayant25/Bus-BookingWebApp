package org.jsp.reservationapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String busNumber;

    @Column(name = "from_location", nullable = false)
    private String from;

    @Column(name = "to_location", nullable = false)
    private String to;

    @Column(nullable = false)
    private double costPerSeat;

    @Column(nullable = false)
    private int availableSeats; // Added field

    @Column(nullable = false)
    private LocalDate dateOfDeparture;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}
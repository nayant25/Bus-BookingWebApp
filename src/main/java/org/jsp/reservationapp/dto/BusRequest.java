package org.jsp.reservationapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BusRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Bus number is mandatory")
    private String busNumber;

    @NotBlank(message = "From location is mandatory")
    private String from;

    @NotBlank(message = "To location is mandatory")
    private String to;

    @Positive(message = "Cost per seat must be positive")
    private double costPerSeat;

    @Positive(message = "Available seats must be positive")
    private int availableSeats; // Ensure this is present

    private LocalDate dateOfDeparture;
}
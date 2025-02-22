package org.jsp.reservationapp.controller;

import org.jsp.reservationapp.dto.ResponseStructure;
import org.jsp.reservationapp.dto.TicketResponse;
import org.jsp.reservationapp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/{busId}/{userId}/{numSeats}")
    public ResponseEntity<ResponseStructure<TicketResponse>> bookTicket(
            @PathVariable int busId,
            @PathVariable int userId,
            @PathVariable int numSeats) {
        if (numSeats <= 0) {
            ResponseStructure<TicketResponse> response = new ResponseStructure<>();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Number of seats must be greater than zero");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return ticketService.bookTicket(userId, busId, numSeats);
    }
}
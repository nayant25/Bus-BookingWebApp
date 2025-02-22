package org.jsp.reservationapp.service;

import java.util.Optional;
import org.jsp.reservationapp.dao.BusDao;
import org.jsp.reservationapp.dao.TicketDao;
import org.jsp.reservationapp.dao.UserDao;
import org.jsp.reservationapp.dto.ResponseStructure;
import org.jsp.reservationapp.dto.TicketResponse;
import org.jsp.reservationapp.model.Bus;
import org.jsp.reservationapp.model.Ticket;
import org.jsp.reservationapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private BusDao busDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TicketDao ticketDao;

    @Transactional
    public ResponseEntity<ResponseStructure<TicketResponse>> bookTicket(int userId, int busId, int numberOfSeats) {
        logger.info("Attempting to book ticket for userId: {}, busId: {}, numberOfSeats: {}", userId, busId, numberOfSeats);

        ResponseStructure<TicketResponse> response = new ResponseStructure<>();

        // Check if user and bus exist
        Optional<User> userOpt = userDao.findById(userId);
        Optional<Bus> busOpt = busDao.findById(busId);

        if (userOpt.isEmpty()) {
            logger.warn("User with ID {} not found", userId);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("User with ID " + userId + " not found");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (busOpt.isEmpty()) {
            logger.warn("Bus with ID {} not found", busId);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Bus with ID " + busId + " not found");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();
        Bus bus = busOpt.get();

        // Check seat availability
        if (bus.getAvailableSeats() < numberOfSeats) {
            logger.warn("Not enough seats available for busId {}. Available: {}, Requested: {}", 
                        busId, bus.getAvailableSeats(), numberOfSeats);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Not enough seats available. Available: " + bus.getAvailableSeats());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // Create and save the ticket
            Ticket ticket = Ticket.builder()
                    .user(user)
                    .bus(bus)
                    .numberOfSeatsBooked(numberOfSeats)
                    .cost(bus.getCostPerSeat() * numberOfSeats)
                    .status("Confirmed")
                    .build();

            ticket = ticketDao.saveTicket(ticket);
            logger.info("Ticket saved with ID: {}", ticket.getId());

            // Update available seats
            bus.setAvailableSeats(bus.getAvailableSeats() - numberOfSeats);
            busDao.saveBus(bus);
            logger.info("Bus {} updated. New available seats: {}", busId, bus.getAvailableSeats());

            // Prepare response
            TicketResponse ticketResponse = mapToTicketResponse(ticket, bus, user);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Ticket booked successfully");
            response.setData(ticketResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to book ticket for userId: {}, busId: {}. Error: {}", userId, busId, e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Booking failed due to server error: " + e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TicketResponse mapToTicketResponse(Ticket ticket, Bus bus, User user) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .username(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .phone(user.getPhone())
                .busName(bus.getName())
                .busNumber(bus.getBusNumber())
                .source(bus.getFrom())
                .destination(bus.getTo())
                .dateOfBooking(ticket.getDateOfBooking())
                .dateOfDeparture(bus.getDateOfDeparture())
                .numberOfSeatsBooked(ticket.getNumberOfSeatsBooked())
                .cost(ticket.getCost())
                .status(ticket.getStatus())
                .build();
    }
}
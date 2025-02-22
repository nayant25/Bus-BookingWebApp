package org.jsp.reservationapp.dao;

import org.jsp.reservationapp.model.Ticket;
import org.jsp.reservationapp.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDao {

    private static final Logger logger = LoggerFactory.getLogger(TicketDao.class);

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket saveTicket(Ticket ticket) {
        logger.info("Saving ticket for busId: {}, userId: {}", 
                    ticket.getBus().getId(), ticket.getUser().getId());
        Ticket savedTicket = ticketRepository.save(ticket);
        logger.info("Ticket saved with ID: {}", savedTicket.getId());
        return savedTicket;
    }
}
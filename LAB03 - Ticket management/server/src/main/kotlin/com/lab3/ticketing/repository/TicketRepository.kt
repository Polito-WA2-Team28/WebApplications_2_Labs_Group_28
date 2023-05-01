package com.lab3.ticketing.repository

import com.lab3.server.model.Customer
import com.lab3.ticketing.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Int> {
}
package com.lab3.ticketing.service

import com.lab3.ticketing.repository.TicketRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TicketCustomerService @Autowired constructor(private val ticketRepository:TicketRepository) : TicketService {
}
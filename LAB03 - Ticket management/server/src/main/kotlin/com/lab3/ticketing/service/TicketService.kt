package com.lab3.ticketing.service

import com.lab3.server.dto.CustomerDTO
import com.lab3.server.dto.ProductDTO
import com.lab3.server.model.Customer
import com.lab3.server.model.Product
import com.lab3.ticketing.dto.TicketCreationData
import com.lab3.ticketing.dto.TicketDTO
import com.lab3.ticketing.model.Ticket
import com.lab3.ticketing.util.TicketState

interface TicketService {

    fun getTicketDTOById(id:Long) : TicketDTO?

    fun getTicketModelById(id:Long) : Ticket?

    fun createTicket(ticket: TicketCreationData, customer: Customer, product: Product): TicketDTO?

    fun getAllTickets(): List<TicketDTO>

    fun getAllExpertTickets(expertId: Long): List<TicketDTO>

    fun getAllCustomerTickets(customerId: Long): List<TicketDTO>

    fun changeTicketStatus(ticket:Ticket, newState:TicketState): TicketDTO

}
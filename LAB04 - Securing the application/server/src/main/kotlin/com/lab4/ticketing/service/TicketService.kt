package com.lab4.ticketing.service

import com.lab4.server.model.Customer
import com.lab4.server.model.Product
import com.lab4.ticketing.dto.TicketCreationData
import com.lab4.ticketing.dto.TicketDTO
import com.lab4.ticketing.model.Ticket
import com.lab4.ticketing.util.TicketState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TicketService {

    fun getTicketDTOById(id:Long) : TicketDTO?

    fun getTicketModelById(id:Long) : Ticket?

    fun createTicket(ticket: TicketCreationData, customer: Customer, product: Product): TicketDTO?

    fun getAllTickets(): List<TicketDTO>

    fun getAllExpertTickets(expertId: Long): List<TicketDTO>

    fun changeTicketStatus(ticket:Ticket, newState:TicketState): TicketDTO

    fun removeTicketById(ticketId: Long): Unit

    fun getAllTicketsWithPaging(pageable: Pageable): Page<TicketDTO>

    fun getAllTicketsWithPagingByCustomerId(customerId: Long, pageable: Pageable): Page<TicketDTO>
    fun getAllTicketsWithPagingByExpertId(expertId: Long, pageable: Pageable): Page<TicketDTO>

}
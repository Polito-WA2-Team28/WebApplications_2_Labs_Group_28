package com.lab5.ticketing.service

import com.lab5.server.model.Customer
import com.lab5.server.model.Product
import com.lab5.ticketing.dto.MessageDTO
import com.lab5.ticketing.dto.MessageObject
import com.lab5.ticketing.dto.TicketCreationData
import com.lab5.ticketing.dto.TicketDTO
import com.lab5.ticketing.model.Ticket
import com.lab5.ticketing.util.TicketState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface TicketService {

    fun getTicketDTOById(id:Long) : TicketDTO?

    fun getTicketModelById(id:Long) : Ticket?

    fun createTicket(ticket: TicketCreationData, customer: Customer, product: Product): TicketDTO?

    fun getAllTickets(): List<TicketDTO>

    fun getAllExpertTickets(expertId: UUID): List<TicketDTO>

    fun changeTicketStatus(ticket:Ticket, newState:TicketState): TicketDTO

    fun removeTicketById(ticketId: Long): Unit

    fun getAllTicketsWithPaging(pageable: Pageable): Page<TicketDTO>

    fun getAllTicketsWithPagingByCustomerId(customerId: UUID, pageable: Pageable): Page<TicketDTO>
    fun getAllTicketsWithPagingByExpertId(expertId: UUID, pageable: Pageable): Page<TicketDTO>

    fun sendTicketMessage(message: MessageObject, ticketId: Long): MessageDTO

}
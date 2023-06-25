package com.final_project.ticketing.service

import com.final_project.server.dto.CustomerDTO
import com.final_project.server.dto.ProductDTO
import com.final_project.server.model.Customer
import com.final_project.server.model.Product
import com.final_project.ticketing.dto.MessageDTO
import com.final_project.ticketing.dto.MessageObject
import com.final_project.ticketing.dto.TicketCreationData
import com.final_project.ticketing.dto.TicketDTO
import com.final_project.ticketing.model.Ticket
import com.final_project.ticketing.util.TicketState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.UUID

interface TicketService {

    fun getTicketDTOById(id:Long) : TicketDTO?

    fun createTicket(ticketDTO: TicketCreationData, customerDTO: CustomerDTO, productDTO: ProductDTO): TicketDTO?

    fun getAllExpertTickets(expertId: UUID): List<TicketDTO>

    fun changeTicketStatus(ticketId: Long, newState:TicketState)

    fun assignTicketToExpert(ticketId: Long, expertId: UUID)

    fun relieveExpertFromTicket(ticketId: Long)

    fun removeTicketById(ticketId: Long): Unit

    fun getAllTicketsWithPaging(pageable: Pageable): Page<TicketDTO>

    fun getAllTicketsWithPagingByCustomerId(customerId: UUID, pageable: Pageable): Page<TicketDTO>
    fun getAllTicketsWithPagingByExpertId(expertId: UUID, pageable: Pageable): Page<TicketDTO>


    fun sendTicketMessage(message: MessageObject, ticketId: Long, sender: String?): MessageDTO

    fun getAllMessagesWithPagingByTicketId(
        ticketId: Long,
        pageable: Pageable
    ): Page<MessageDTO>

}
package com.lab3.ticketing.service

import com.lab3.server.model.Customer
import com.lab3.server.model.Product
import com.lab3.ticketing.dto.TicketCreationData
import com.lab3.ticketing.dto.TicketDTO
import com.lab3.ticketing.dto.toDTO
import com.lab3.ticketing.model.Ticket
import com.lab3.ticketing.model.toModel
import com.lab3.ticketing.repository.TicketRepository
import com.lab3.ticketing.util.TicketState
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl @Autowired constructor(private val ticketRepository: TicketRepository) : TicketService{

    override fun getTicketDTOById(id: Long): TicketDTO? {
        return ticketRepository.findByIdOrNull(id)?.toDTO()
    }

    override fun getTicketModelById(id: Long): Ticket? {
        return ticketRepository.findByIdOrNull(id)
    }

    @Transactional
    override fun createTicket(ticket: TicketCreationData, customer: Customer, product: Product): TicketDTO? {
        return ticketRepository.save(ticket.toModel(customer, product)).toDTO()
    }

    override fun getAllTickets(): List<TicketDTO> {
        return ticketRepository.findAll().map{it.toDTO()}
    }

    override fun getAllExpertTickets(expertId: Long): List<TicketDTO> {
        return ticketRepository.findByExpertId(expertId).map{it.toDTO()}
    }

    override fun changeTicketStatus(ticket: Ticket, newState: TicketState): TicketDTO {
        ticket.state = newState
        return ticketRepository.save(ticket).toDTO()
    }

    override fun removeTicketById(ticketId: Long): Unit {
        ticketRepository.deleteById(ticketId)
    }

    override fun getAllTicketsWithPaging(pageable: Pageable): Page<TicketDTO> {
        return ticketRepository.findAll(pageable)
            .map {
                it.toDTO()
            }
    }

    override fun getAllTicketsWithPagingByCustomerId(customerId: Long, pageable: Pageable): Page<TicketDTO> {
        return ticketRepository.findAllByCustomerId(customerId, pageable)
            .map {
                it.toDTO()
            }
    }

    override fun getAllTicketsWithPagingByExpertId(expertId: Long, pageable: Pageable): Page<TicketDTO> {
        return ticketRepository.findAllByExpertId(expertId, pageable)
            .map {
                it.toDTO()
            }
    }

}
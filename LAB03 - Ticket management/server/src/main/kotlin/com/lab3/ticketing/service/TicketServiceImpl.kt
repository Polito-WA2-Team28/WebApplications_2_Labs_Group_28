package com.lab3.ticketing.service

import com.lab3.server.dto.CustomerDTO
import com.lab3.server.dto.ProductDTO
import com.lab3.server.dto.toDTO
import com.lab3.server.model.Customer
import com.lab3.server.model.Product
import com.lab3.ticketing.dto.TicketCreationData
import com.lab3.ticketing.dto.TicketDTO
import com.lab3.ticketing.dto.toDTO
import com.lab3.ticketing.model.toModel
import com.lab3.ticketing.repository.TicketRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl @Autowired constructor(private val ticketRepository: TicketRepository) : TicketService{

    override fun getTicketById(id: Long): TicketDTO? {
        return ticketRepository.findByIdOrNull(id)?.toDTO()
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

    override fun getAllCustomerTickets(customerId: Long): List<TicketDTO> {
        return ticketRepository.findByCustomerId(customerId).map{it.toDTO()}
    }




}
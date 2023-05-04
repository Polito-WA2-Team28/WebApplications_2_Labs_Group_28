package com.lab3.ticketing.service

import com.lab3.server.dto.CustomerDTO
import com.lab3.server.dto.ProductDTO
import com.lab3.server.model.Customer
import com.lab3.server.model.Product
import com.lab3.ticketing.dto.TicketCreationData
import com.lab3.ticketing.dto.TicketDTO

interface TicketService {

    fun getTicketById(id:Long) : TicketDTO?

    fun createTicket(ticket: TicketCreationData, customer: Customer, product: Product): TicketDTO?
}
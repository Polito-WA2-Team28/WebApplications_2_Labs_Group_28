package com.lab4.ticketing.controller

import com.lab4.security.config.SecurityConfig
import com.lab4.server.dto.CustomerDTO
import com.lab4.server.dto.toDTO
import com.lab4.server.exception.Exception
import com.lab4.server.model.Customer
import com.lab4.server.model.Product
import com.lab4.server.service.CustomerService
import com.lab4.server.service.ProductService
import com.lab4.ticketing.dto.TicketCreationData
import com.lab4.ticketing.dto.TicketDTO
import com.lab4.ticketing.exception.TicketException
import com.lab4.ticketing.service.TicketService
import com.lab4.ticketing.util.TicketState
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
class TicketCustomerController @Autowired constructor(
    val ticketService: TicketService,
    val customerService: CustomerService,
    val productService: ProductService,
    val securityConfig: SecurityConfig
) {

    @PostMapping("/api/customers/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTicket(
        @RequestBody @Valid ticket: TicketCreationData,
        br: BindingResult
    ): TicketDTO {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim())

        val customer: Customer = customerService.getCustomerById(customerId)
            ?: throw Exception.CustomerNotFoundException("Customer not found.")
        val product: Product = productService.getProductBySerialNumber(ticket.serialNumber)
            ?: throw Exception.ProductNotFoundException("Product not found.")


        if (product.owner == customer) {
            return ticketService.createTicket(ticket, customer, product)
                ?: throw TicketException.TicketCreationException("Ticket creation error.")
        }
        throw Exception.CustomerNotOwnerException("Customer is not the owner of this product.")
    }

    @GetMapping("/api/customers/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(
        @RequestParam("pageNo", defaultValue = "0") pageNo: Int
    ): Page<TicketDTO> {

        val customerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* checking that customer exists */
        customerService.getCustomerById(customerId)
            ?: throw Exception.CustomerNotFoundException("Customer not found.")

        /* computing page and retrieving all the tickets corresponding to this customer */
        var page: Pageable = PageRequest.of(pageNo, 3)
        return ticketService.getAllTicketsWithPagingByCustomerId(customerId, page)
    }

    @GetMapping("/api/customers/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim())

        var ticket = ticketService.getTicketDTOById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")

        if(ticket.customerId == customerId){
            return ticket
        }
        throw TicketException.TicketForbiddenException("Forbidden.")
    }

    @PatchMapping("/api/customers/tickets/{ticketId}/reopen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reopenTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim())

        var ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        var allowedStates = mutableSetOf(TicketState.CLOSED, TicketState.RESOLVED)

        if(ticket.customer.getId() != customerId || !allowedStates.contains(ticket.state)) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        return ticketService.changeTicketStatus(ticket, TicketState.REOPENED)
    }

    @PatchMapping("/api/customers/tickets/{ticketId}/compileSurvey")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun compileTicketSurvey(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim())

        var ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")

        if (ticket.customer.getId() != customerId) {
            throw TicketException.TicketForbiddenException("Forbidden.")
        } else if (ticket.state != TicketState.RESOLVED) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }
        return ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }
}
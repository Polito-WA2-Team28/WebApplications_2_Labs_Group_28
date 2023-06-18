package com.final_project.ticketing.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.controller.CustomerProductController
import com.final_project.server.exception.Exception
import com.final_project.server.model.*
import com.final_project.server.service.*
import com.final_project.ticketing.dto.*
import com.final_project.ticketing.exception.TicketException
import com.final_project.ticketing.service.TicketService
import com.final_project.ticketing.util.TicketState
import com.final_project.server.controller.StaffController
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.math.log

@RestController
@Observed
class TicketCustomerController @Autowired constructor(
    val ticketService: TicketService,
    val customerService: CustomerService,
    val customerProductController: CustomerProductController,
    val securityConfig: SecurityConfig
) {

    val logger: Logger = LoggerFactory.getLogger(TicketCustomerController::class.java)

    @PostMapping("/api/customers/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTicket(
        @RequestBody @Valid ticket: TicketCreationData,
        br: BindingResult
    ): TicketDTO {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        val customer: Customer? = customerService.getCustomerById(customerId)
        if (customer == null) {
            logger.error("Endpoint: /api/customers/tickets Error: Customer not found.")
            throw Exception.CustomerNotFoundException("Customer not found.")
        }
        val product: Product? = customerProductController.getProductBySerialNumber(customerId, ticket.serialNumber)
        if (product == null) {
            logger.error("Endpoint: /api/customers/tickets Error: Product not found.")
            throw Exception.ProductNotFoundException("Product not found.")
        }


        if (product.owner == customer) {
            val createdTicket = ticketService.createTicket(ticket, customer, product)
            if (createdTicket == null) {
                logger.error("Endpoint: /api/customers/tickets Error: Ticket creation error.")
                throw TicketException.TicketCreationException("Ticket creation error.")
            }
            return createdTicket
        }
        else {
            logger.error("Endpoint: /api/customers/tickets Error: Customer is not the owner of this product.")
            throw Exception.CustomerNotOwnerException("Customer is not the owner of this product.")
        }
    }

    @GetMapping("/api/customers/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(
        @RequestParam("pageNo", defaultValue = "0") pageNo: Int
    ): Page<TicketDTO> {

        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        /* checking that customer exists */
        val customer = customerService.getCustomerById(customerId)
            if(customer == null) {
                logger.error("Endpoint: /api/customers/tickets Error: Customer not found.")
                throw Exception.CustomerNotFoundException("Customer not found.")
            }

        /* computing page and retrieving all the tickets corresponding to this customer */
        val page: Pageable = PageRequest.of(pageNo, 3)
        return ticketService.getAllTicketsWithPagingByCustomerId(customerId, page)
    }

    @GetMapping("/api/customers/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        val ticket = ticketService.getTicketDTOById(ticketId)
        if(ticket == null){
            logger.error("Endpoint: /api/customers/tickets/$ticketId Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        if (ticket.customerId == customerId)
            return ticket

        logger.error("Endpoint: /api/customers/tickets/$ticketId Error: Forbidden.")
        throw TicketException.TicketForbiddenException("Forbidden.")
    }

    @PatchMapping("/api/customers/tickets/{ticketId}/reopen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reopenTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        val ticket = ticketService.getTicketModelById(ticketId)
        if(ticket == null){
            logger.error("Endpoint: /api/customers/tickets/$ticketId/reopen Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }


        val allowedStates = mutableSetOf(TicketState.CLOSED, TicketState.RESOLVED)

        if (ticket.customer.id != customerId || !allowedStates.contains(ticket.state)) {
            logger.error("Endpoint: /api/customers/tickets/$ticketId/reopen Error: Forbidden.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        return ticketService.changeTicketStatus(ticket, TicketState.REOPENED)
    }

    @PatchMapping("/api/customers/tickets/{ticketId}/compileSurvey")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun compileTicketSurvey(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        val ticket = ticketService.getTicketModelById(ticketId)
        if(ticket==null){
            logger.error("Endpoint: /api/customers/tickets/$ticketId/compileSurvey Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        if (ticket.customer.id != customerId) {
            logger.error("Endpoint: /api/customers/tickets/$ticketId/compileSurvey Error: Forbidden.")
            throw TicketException.TicketForbiddenException("Forbidden.")
        }
        else if (ticket.state != TicketState.RESOLVED) {
            logger.error("Endpoint: /api/customers/tickets/$ticketId/compileSurvey Error: Invalid ticket status for this operation.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        return ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }


    @PostMapping("/api/customers/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    fun sendTicketMessage(@ModelAttribute message:MessageObject,
                          @PathVariable ticketId: Long):MessageDTO{

        // Add checks:
        // if Customer => check ticket ownership
        // if Expert => check if ticket is assigned (in TicketExpertController)

        // retrieve sender username
        val sender = securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.USERNAME)

        // call TicketService to save message and attachments
        return ticketService.sendTicketMessage(message, ticketId, sender)

    }


    @GetMapping("/api/customers/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketMessages(){
        TODO()
    }
}
package com.lab5.ticketing.controller

import com.lab5.security.config.SecurityConfig
import com.lab5.server.exception.Exception
import com.lab5.server.model.Expert
import com.lab5.server.service.ExpertService
import com.lab5.ticketing.dto.TicketDTO
import com.lab5.ticketing.exception.TicketException
import com.lab5.ticketing.model.Ticket
import com.lab5.ticketing.service.TicketService
import com.lab5.ticketing.util.TicketState
import io.micrometer.observation.annotation.Observed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
@Observed
class TicketExpertController @Autowired constructor(
    val ticketService: TicketService,
    val expertService: ExpertService,
    val securityConfig: SecurityConfig
) {

    @GetMapping("/api/experts/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@RequestParam("pageNo", defaultValue = "0") pageNo: Int
    ): Page<TicketDTO> {
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* checking that the expert exists */
        expertService.getExpertById(expertId)
            ?: throw Exception.ExpertNotFoundException("Expert not found.")

        /* computing page and retrieving all the tickets corresponding to this expert */
        var page: Pageable = PageRequest.of(pageNo, 3)
        return ticketService.getAllTicketsWithPagingByExpertId(expertId, page)

    }

    @GetMapping("/api/experts/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim())
        
        /* checking that the expert exists */
        expertService.getExpertById(expertId)
            ?: throw Exception.ExpertNotFoundException("Expert not found.")

        return ticketService.getTicketDTOById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
    }

    @PatchMapping("/api/experts/tickets/{ticketId}/resolve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resolveTicket(
        @PathVariable("ticketId") ticketId: Long
    ) {
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim())

        var ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        var expert: Expert = ticket.expert
            ?: throw Exception.ExpertNotFoundException("Expert not found.")
        var allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED, TicketState.IN_PROGRESS)

        if (expert.id != expertId) {
            throw Exception.ExpertNotFoundException("Expert not found.")
        } else if (!allowedStates.contains(ticket.state)) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }
        ticketService.changeTicketStatus(ticket, TicketState.RESOLVED)
    }

    @PatchMapping("/api/experts/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket(@PathVariable("ticketId") ticketId:Long){
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim())

        var ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        var expert: Expert = ticket.expert
            ?: throw Exception.ExpertNotFoundException("Expert not found.")
        var allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED)

        if (expert.id != expertId) {
            throw Exception.ExpertNotFoundException("Expert not found.")
        } else if (!allowedStates.contains(ticket.state)) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }
        ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }
}
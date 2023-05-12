package com.lab4.ticketing.controller

import com.lab4.server.exception.Exception
import com.lab4.server.model.Expert
import com.lab4.server.service.ExpertService
import com.lab4.ticketing.dto.TicketDTO
import com.lab4.ticketing.exception.TicketException
import com.lab4.ticketing.model.Ticket
import com.lab4.ticketing.service.TicketService
import com.lab4.ticketing.util.TicketState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
class TicketExpertController @Autowired constructor(
    val ticketService: TicketService,
    val expertService: ExpertService
) {

    @GetMapping("/api/experts/{expertId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(
        @PathVariable("expertId") expertId: Long,
        @RequestParam("pageNo", defaultValue = "0") pageNo: Int
    ): Page<TicketDTO> {

        /* checking that the expert exists */
        expertService.getExpertById(expertId)
            ?: throw Exception.ExpertNotFoundException("Expert not found.")

        /* computing page and retrieving all the tickets corresponding to this expert */
        var page: Pageable = PageRequest.of(pageNo, 3)
        return ticketService.getAllTicketsWithPagingByExpertId(expertId, page)

    }

    @GetMapping("/api/experts/{expertId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(
        @PathVariable("expertId") expertId: Long,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        
        /* checking that the expert exists */
        expertService.getExpertById(expertId)
            ?: throw Exception.ExpertNotFoundException("Expert not found.")

        return ticketService.getTicketDTOById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
    }

    @PatchMapping("/api/experts/{expertId}/tickets/{ticketId}/resolve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resolveTicket(
        @PathVariable("expertId") expertId: Long,
        @PathVariable("ticketId") ticketId: Long
    ) {

        var ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        var expert: Expert = ticket.expert
            ?: throw Exception.ExpertNotFoundException("Expert not found.")
        var allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED, TicketState.IN_PROGRESS)

        if (expert.getId() != expertId) {
            throw Exception.ExpertNotFoundException("Expert not found.")
        } else if (!allowedStates.contains(ticket.state)) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }
        ticketService.changeTicketStatus(ticket, TicketState.RESOLVED)
    }

    @PatchMapping("/api/experts/{expertId}/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket(@PathVariable("expertId") expertId:Long,
                     @PathVariable("ticketId") ticketId:Long){

        var ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        var expert: Expert = ticket.expert
            ?: throw Exception.ExpertNotFoundException("Expert not found.")
        var allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED)

        if (expert.getId() != expertId) {
            throw Exception.ExpertNotFoundException("Expert not found.")
        } else if (!allowedStates.contains(ticket.state)) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }
        ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }
}
package com.lab3.ticketing.controller

import com.lab3.ticketing.dto.TicketDTO
import com.lab3.ticketing.exception.Exception
import com.lab3.ticketing.model.Ticket
import com.lab3.ticketing.service.TicketServiceImpl
import com.lab3.ticketing.util.TicketState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketManagerController @Autowired constructor(val ticketService: TicketServiceImpl) {
    @GetMapping("/API/managers/{managerId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@PathVariable("managerId") managerId:Long):List<TicketDTO>{
        return ticketService.getAllTickets()
    }

    @GetMapping("/API/managers/{managerId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("managerId") managerId:Long,
                        @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun assignTicket (
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* retrieve the ticket from the database */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw Exception.TicketNotFoundException("Ticket not found.")

        /* checking the state of the ticket */
        if (ticket.state != TicketState.OPEN) {
            throw Exception.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.IN_PROGRESS)

    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/relieveExpert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun relieveExpert (
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* retrieve the ticket from the database */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw Exception.TicketNotFoundException("Ticket not found.")

        /* checking the state of the ticket */
        if (ticket.state != TicketState.IN_PROGRESS) {
            throw Exception.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.OPEN)
    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket (
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* retrieve the ticket from the database */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw Exception.TicketNotFoundException("Ticket not found.")

        /* checking the state of the ticket */
        if (ticket.state != TicketState.OPEN || ticket.state != TicketState.RESOLVED || ticket.state != TicketState.REOPENED) {
            throw Exception.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/resumeProgress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resumeTicketProgress (
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* retrieve the ticket from the database */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw Exception.TicketNotFoundException("Ticket not found.")

        /* checking the state of the ticket */
        if (ticket.state != TicketState.REOPENED) {
            throw Exception.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.IN_PROGRESS)
    }

    @DeleteMapping("/API/managers/{managerId}/tickets/{ticketId}/remove")
    @ResponseStatus(HttpStatus.OK)
    fun removeTicket(@PathVariable("managerId") managerId:Long,
                     @PathVariable("ticketId") ticketId:Long){

    }
}
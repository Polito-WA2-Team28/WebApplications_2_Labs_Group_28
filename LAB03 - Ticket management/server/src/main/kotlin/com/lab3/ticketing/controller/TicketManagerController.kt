package com.lab3.ticketing.controller

import com.lab3.server.exception.Exception
import com.lab3.server.model.Expert
import com.lab3.server.service.ExpertService
import com.lab3.server.service.ManagerService
import com.lab3.ticketing.dto.TicketDTO
import com.lab3.ticketing.dto.TicketUpdateData
import com.lab3.ticketing.exception.TicketException
import com.lab3.ticketing.model.Ticket
import com.lab3.ticketing.service.TicketServiceImpl
import com.lab3.ticketing.util.TicketState
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketManagerController @Autowired constructor(
    val ticketService: TicketServiceImpl,
    val managerService: ManagerService,
    val expertService: ExpertService
) {
    @GetMapping("/API/managers/{managerId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(
        @PathVariable("managerId") managerId: Long
    ): List<TicketDTO> {

        /* checking that the manager exists */
        if (managerService.getManagerById(managerId) == null) {
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* retrieving all the tickets */
        return ticketService.getAllTickets()
    }

    @GetMapping("/API/managers/{managerId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* checking that the manager exists */
        if (managerService.getManagerById(managerId) == null) {
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* retrieving the ticket */
        ticketService.getTicketDTOById(ticketId)?.let {
            return it
        }
        throw TicketException.TicketNotFoundException("Ticket not found.")
    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun assignTicket (
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long,
        @RequestBody @Valid ticketUpdateData: TicketUpdateData
    ): TicketDTO? {

        /* retrieve the ticket from the database and checking the status */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        if (ticket.state != TicketState.OPEN) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* retrieving the expert and checking that manager exists */
        val expert: Expert? = expertService.getExpertById(ticketUpdateData.expertId)
            ?: throw Exception.ExpertNotFoundException("Expert not found.")
        if (managerService.getManagerById(managerId) == null) {
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* assign the expert to the ticket */
        if (expert != null) {
            ticket.assignExpert(expert) // do I need also to implement the reverse-mapping operation?
                                        // i.e. expert.assignTicket(ticket)? But we don't have the list of tickets...
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

        /* retrieve the ticket from the database and checking the state of the ticket */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        if (ticket.state != TicketState.IN_PROGRESS) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* checking if manager exists */
        if (managerService.getManagerById(managerId) == null) {
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* relieving the expert from the ticket */
        ticket.relieveExpert()

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.OPEN)
    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket (
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* retrieve the ticket from the database and checking the state of the ticket */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        if (ticket.state != TicketState.OPEN && ticket.state != TicketState.RESOLVED && ticket.state != TicketState.REOPENED) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* checking if manager exists */
        if (managerService.getManagerById(managerId) == null) {
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/resumeProgress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resumeTicketProgress (
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long,
        @RequestBody @Valid ticketUpdateData: TicketUpdateData
    ): TicketDTO? {

        /* retrieve the ticket from the database and checking the state of the ticket */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        if (ticket.state != TicketState.REOPENED) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* retrieving the expert and checking that manager exists */
        val expert: Expert? = expertService.getExpertById(ticketUpdateData.expertId)
            ?: throw Exception.ExpertNotFoundException("Expert not found.")
        if (managerService.getManagerById(managerId) == null) {
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* assign the expert to the ticket */
        if (expert != null) {
            ticket.assignExpert(expert) // do I need also to implement the reverse-mapping operation?
                                        // i.e. expert.assignTicket(ticket)? But we don't have the list of tickets...
        }

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.IN_PROGRESS)
    }

    @DeleteMapping("/API/managers/{managerId}/tickets/{ticketId}/remove")
    @ResponseStatus(HttpStatus.OK)
    fun removeTicket(
        @PathVariable("managerId") managerId: Long,
        @PathVariable("ticketId") ticketId: Long
    ): Unit {

        /* checking that manager exists */
        if (managerService.getManagerById(managerId) == null) {
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* removing the ticket from the database */
        ticketService.removeTicketById(ticketId)

    }
}
package com.lab4.ticketing.controller

import com.lab4.server.exception.Exception
import com.lab4.server.model.Expert
import com.lab4.server.service.ExpertService
import com.lab4.server.service.ManagerService
import com.lab4.ticketing.dto.TicketDTO
import com.lab4.ticketing.dto.TicketUpdateData
import com.lab4.ticketing.exception.TicketException
import com.lab4.ticketing.model.Ticket
import com.lab4.ticketing.service.TicketService
import com.lab4.ticketing.util.TicketState
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class TicketManagerController @Autowired constructor(
    val ticketService: TicketService,
    val managerService: ManagerService,
    val expertService: ExpertService
) {
    @GetMapping("/api/managers/{managerId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(
        @PathVariable("managerId") managerId: UUID,
        @RequestParam("pageNo", defaultValue = "0") pageNo: Int
    ): Page<TicketDTO> {

        /* checking that the manager exists */
        managerService.getManagerById(managerId)
            ?: throw Exception.ManagerNotFoundException("Manager not found.")

        /* computing page and retrieving all the tickets */
        var page: Pageable = PageRequest.of(pageNo, 3)
        return ticketService.getAllTicketsWithPaging(page)
    }

    @GetMapping("/api/managers/{managerId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(
        @PathVariable("managerId") managerId: UUID,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* checking that the manager exists */
        managerService.getManagerById(managerId)
            ?: throw Exception.ManagerNotFoundException("Manager not found.")

        /* retrieving the ticket */
        ticketService.getTicketDTOById(ticketId)?.let {
            return it
        }
        throw TicketException.TicketNotFoundException("Ticket not found.")
    }

    @PatchMapping("/api/managers/{managerId}/tickets/{ticketId}/assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun assignTicket (
        @PathVariable("managerId") managerId: UUID,
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
        val expert: Expert = expertService.getExpertById(ticketUpdateData.expertId)
            ?: throw Exception.ExpertNotFoundException("Expert not found.")
        managerService.getManagerById(managerId)
            ?: throw Exception.ManagerNotFoundException("Manager not found.")

        /* assign the expert to the ticket */
        ticket.assignExpert(expert)

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.IN_PROGRESS)

    }

    @PatchMapping("/api/managers/{managerId}/tickets/{ticketId}/relieveExpert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun relieveExpert (
        @PathVariable("managerId") managerId: UUID,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* retrieve the ticket from the database and checking the state of the ticket */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        if (ticket.state != TicketState.IN_PROGRESS) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* checking if manager exists */
        managerService.getManagerById(managerId)
            ?: throw Exception.ManagerNotFoundException("Manager not found.")

        /* relieving the expert from the ticket */
        ticket.relieveExpert()

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.OPEN)
    }

    @PatchMapping("/api/managers/{managerId}/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket (
        @PathVariable("managerId") managerId: UUID,
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        /* retrieve the ticket from the database and checking the state of the ticket */
        val ticket: Ticket = ticketService.getTicketModelById(ticketId)
            ?: throw TicketException.TicketNotFoundException("Ticket not found.")
        if (ticket.state != TicketState.OPEN && ticket.state != TicketState.RESOLVED && ticket.state != TicketState.REOPENED) {
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* checking if manager exists */
        managerService.getManagerById(managerId)
            ?: throw Exception.ManagerNotFoundException("Manager not found.")

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }

    @PatchMapping("/api/managers/{managerId}/tickets/{ticketId}/resumeProgress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resumeTicketProgress (
        @PathVariable("managerId") managerId: UUID,
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
        val expert: Expert = expertService.getExpertById(ticketUpdateData.expertId)
            ?: throw Exception.ExpertNotFoundException("Expert not found.")
        managerService.getManagerById(managerId)
            ?: throw Exception.ManagerNotFoundException("Manager not found.")

        /* assign the expert to the ticket */
        ticket.assignExpert(expert)

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.IN_PROGRESS)
    }

    @DeleteMapping("/api/managers/{managerId}/tickets/{ticketId}/remove")
    @ResponseStatus(HttpStatus.OK)
    fun removeTicket(
        @PathVariable("managerId") managerId: UUID,
        @PathVariable("ticketId") ticketId: Long
    ): Unit {

        /* checking that manager exists */
        managerService.getManagerById(managerId)
            ?: throw Exception.ManagerNotFoundException("Manager not found.")

        /* removing the ticket from the database */
        ticketService.removeTicketById(ticketId)

    }
}
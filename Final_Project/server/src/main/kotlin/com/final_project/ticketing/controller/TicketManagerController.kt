package com.lab5.ticketing.controller

import com.lab5.security.config.SecurityConfig
import com.lab5.server.exception.Exception
import com.lab5.server.model.Expert
import com.lab5.server.service.ExpertService
import com.lab5.server.service.ManagerService
import com.lab5.ticketing.dto.TicketDTO
import com.lab5.ticketing.dto.TicketUpdateData
import com.lab5.ticketing.exception.TicketException
import com.lab5.ticketing.model.Ticket
import com.lab5.ticketing.service.TicketService
import com.lab5.ticketing.util.TicketState
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@Observed
class TicketManagerController @Autowired constructor(
    val ticketService: TicketService,
    val managerService: ManagerService,
    val expertService: ExpertService,
    val securityConfig: SecurityConfig
) {

    val logger: Logger = LoggerFactory.getLogger(TicketManagerController::class.java)

    @GetMapping("/api/managers/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@RequestParam("pageNo", defaultValue = "0") pageNo: Int
    ): Page<TicketDTO> {
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* checking that the manager exists */
        val manager = managerService.getManagerById(managerId)
        if (manager == null) {
            logger.error("Endpoint: /api/managers/tickets Error: Manager not found.")
            throw Exception.ManagerNotFoundException("Manager not found.")
        }


        /* computing page and retrieving all the tickets */
        var page: Pageable = PageRequest.of(pageNo, 3)
        return ticketService.getAllTicketsWithPaging(page)
    }

    @GetMapping("/api/managers/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* checking that the manager exists */
        val manager = managerService.getManagerById(managerId)
        if (manager == null) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId} Error: Manager not found.")
            throw Exception.ManagerNotFoundException("Manager not found.")
        }


        /* retrieving the ticket */
        ticketService.getTicketDTOById(ticketId)?.let {
            return it
        }
        logger.error("Endpoint: /api/managers/tickets/{ticketId} Error: Ticket not found.")
        throw TicketException.TicketNotFoundException("Ticket not found.")
    }

    @PatchMapping("/api/managers/tickets/{ticketId}/assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun assignTicket (@PathVariable("ticketId") ticketId: Long,
                      @RequestBody @Valid ticketUpdateData: TicketUpdateData
    ): TicketDTO? {
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* retrieve the ticket from the database and checking the status */
        val ticket = ticketService.getTicketModelById(ticketId)
        if (ticket == null) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/assign Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        if (ticket.state != TicketState.OPEN) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/assign Error: Invalid ticket status for this operation.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* retrieving the expert and checking that manager exists */
        val expert = expertService.getExpertById(ticketUpdateData.expertId)
        if (expert == null) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/assign Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        val manager = managerService.getManagerById(managerId)
        if (manager == null) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/assign Error: Manager not found.")
            throw Exception.ManagerNotFoundException("Manager not found.")
        }

        /* assign the expert to the ticket */
        ticket.assignExpert(expert)

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.IN_PROGRESS)

    }

    @PatchMapping("/api/managers/tickets/{ticketId}/relieveExpert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun relieveExpert (@PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* retrieve the ticket from the database and checking the state of the ticket */
        val ticket = ticketService.getTicketModelById(ticketId)
        if (ticket == null) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/relieveExpert Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        if (ticket.state != TicketState.IN_PROGRESS) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/relieveExpert Error: Invalid ticket status for this operation.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* checking if manager exists */
        val manager = managerService.getManagerById(managerId)
        if (manager == null) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/relieveExpert Error: Manager not found.")
            throw Exception.ManagerNotFoundException("Manager not found.")
        }


        /* relieving the expert from the ticket */
        ticket.relieveExpert()

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.OPEN)
    }

    @PatchMapping("/api/managers/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket (@PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* retrieve the ticket from the database and checking the state of the ticket */
        val ticket = ticketService.getTicketModelById(ticketId)
        if (ticket == null) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/close Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        if (ticket.state != TicketState.OPEN && ticket.state != TicketState.RESOLVED && ticket.state != TicketState.REOPENED) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/close Error: Invalid ticket status for this operation.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* checking if manager exists */
        val manager = managerService.getManagerById(managerId)
        if (manager == null) {
            logger.error("Endpoint: /api/managers/tickets/{ticketId}/close Error: Manager not found.")
            throw Exception.ManagerNotFoundException("Manager not found.")
        }


        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }

    @PatchMapping("/api/managers/tickets/{ticketId}/resumeProgress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resumeTicketProgress (@PathVariable("ticketId") ticketId: Long,
                              @RequestBody @Valid ticketUpdateData: TicketUpdateData
    ): TicketDTO? {
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* retrieve the ticket from the database and checking the state of the ticket */
        val ticket = ticketService.getTicketModelById(ticketId)
            if(ticket == null){
                logger.error("Endpoint:/api/managers/tickets/{ticketId}/resumeProgress Error: Ticket not found.")
                throw TicketException.TicketNotFoundException("Ticket not found.")
            }
        if (ticket.state != TicketState.REOPENED) {
            logger.error("Endpoint:/api/managers/tickets/{ticketId}/resumeProgress Error: Invalid ticket status for this operation.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        /* retrieving the expert and checking that manager exists */
        val expert = expertService.getExpertById(ticketUpdateData.expertId)
            if(expert == null){
                logger.error("Endpoint:/api/managers/tickets/{ticketId}/resumeProgress Error: Expert not found.")
                throw Exception.ExpertNotFoundException("Expert not found.")
            }
        val manager = managerService.getManagerById(managerId)
            if(manager == null){
                logger.error("Endpoint:/api/managers/tickets/{ticketId}/resumeProgress Error: Manager not found.")
                throw Exception.ManagerNotFoundException("Manager not found.")
            }

        /* assign the expert to the ticket */
        ticket.assignExpert(expert)

        /* change the ticket status */
        return ticketService.changeTicketStatus(ticket, TicketState.IN_PROGRESS)
    }

    @DeleteMapping("/api/managers/tickets/{ticketId}/remove")
    @ResponseStatus(HttpStatus.OK)
    fun removeTicket(@PathVariable("ticketId") ticketId: Long
    ) {
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* checking that manager exists */
        val manager = managerService.getManagerById(managerId)
            if(manager == null) {
                logger.error("Endpoint:/api/managers/tickets/{ticketId}/remove Error: Manager not found.")
                throw Exception.ManagerNotFoundException("Manager not found.")
            }

        /* removing the ticket from the database */
        ticketService.removeTicketById(ticketId)

    }
}
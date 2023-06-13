package com.final_project.ticketing.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.exception.Exception
import com.final_project.server.service.ExpertService
import com.final_project.ticketing.dto.TicketDTO
import com.final_project.ticketing.exception.TicketException
import com.final_project.ticketing.service.TicketService
import com.final_project.ticketing.util.TicketState
import io.micrometer.observation.annotation.Observed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
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

    val logger: Logger = LoggerFactory.getLogger(TicketExpertController::class.java)


    @GetMapping("/api/experts/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@RequestParam("pageNo", defaultValue = "0") pageNo: Int
    ): Page<TicketDTO> {
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        /* checking that the expert exists */
       val expert = expertService.getExpertById(expertId)
        if(expert == null)
        {
            logger.error("Endpoint: /api/experts/tickets\nError: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        /* computing page and retrieving all the tickets corresponding to this expert */
        val page: Pageable = PageRequest.of(pageNo, 3)
        return ticketService.getAllTicketsWithPagingByExpertId(expertId, page)

    }

    @GetMapping("/api/experts/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        
        /* checking that the expert exists */
        val expert = expertService.getExpertById(expertId)
        if(expert == null){
            logger.error("Endpoint: /api/experts/tickets/$ticketId\nError: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        val ticket = ticketService.getTicketDTOById(ticketId)
        if(ticket == null) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId\nError: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }
        return ticket
    }

    @PatchMapping("/api/experts/tickets/{ticketId}/resolve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resolveTicket(@PathVariable("ticketId") ticketId: Long) {
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        val ticket = ticketService.getTicketModelById(ticketId)
        if(ticket == null) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/resolve\nError: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }
        val expert = ticket.expert
        if (expert == null) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/resolve\nError: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        val allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED, TicketState.IN_PROGRESS)

        if (expert.id != expertId){
            logger.error("Endpoint: /api/experts/tickets/$ticketId/resolve\nError: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        else if (!allowedStates.contains(ticket.state)) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/resolve\nError: Invalid ticket status for this operation.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        ticketService.changeTicketStatus(ticket, TicketState.RESOLVED)
    }

    @PatchMapping("/api/experts/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket(@PathVariable("ticketId") ticketId:Long){
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        val ticket = ticketService.getTicketModelById(ticketId)
        if(ticket == null){
            logger.error("Endpoint: /api/experts/tickets/$ticketId/close\nError: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        val expert = ticket.expert
        if(expert == null){
            logger.error("Endpoint: /api/experts/tickets/$ticketId/close\nError: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }
        val allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED)

        if (expert.id != expertId) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/close\nError: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }
        else if (!allowedStates.contains(ticket.state)){
            logger.error("Endpoint: /api/experts/tickets/$ticketId/close\nError: Invalid ticket status for this operation.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }


    @PostMapping("/api/experts/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    fun sendTicketMessage(){

    }


    @GetMapping("/api/experts/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketMessages(){

    }
}
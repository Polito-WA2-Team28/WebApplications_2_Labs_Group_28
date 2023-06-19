package com.final_project.ticketing.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.exception.Exception
import com.final_project.server.service.ExpertService
import com.final_project.ticketing.exception.TicketException
import com.final_project.ticketing.service.TicketService
import com.final_project.ticketing.util.TicketState


import com.final_project.server.model.Expert
import com.final_project.ticketing.dto.*
import com.final_project.ticketing.dto.PageResponseDTO.Companion.toDTO
import com.final_project.ticketing.model.Ticket
import io.micrometer.observation.annotation.Observed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID
import kotlin.math.exp

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
    fun getTickets(@RequestParam("pageNo", defaultValue = "1") pageNo: Int
    ): PageResponseDTO<TicketDTO> {
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))

        /* checking that the expert exists */
       val expert = expertService.getExpertById(expertId)
        if(expert == null)
        {
            logger.error("Endpoint: /api/experts/tickets Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        /* crafting pageable request */
        var result: PageResponseDTO<TicketDTO> = PageResponseDTO()
        val page: Pageable = PageRequest.of(pageNo-1, result.computePageSize())

        result = ticketService.getAllTicketsWithPagingByExpertId(expertId, page).toDTO()
        return result

    }

    @GetMapping("/api/experts/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        
        /* checking that the expert exists */
        val expert = expertService.getExpertById(expertId)
        if(expert == null){
            logger.error("Endpoint: /api/experts/tickets/$ticketId Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        val ticket = ticketService.getTicketDTOById(ticketId)
        if(ticket == null) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId Error: Ticket not found.")
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
            logger.error("Endpoint: /api/experts/tickets/$ticketId/resolve Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }
        val expert = ticket.expert
        if (expert == null) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/resolve Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        val allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED, TicketState.IN_PROGRESS)

        if (expert.id != expertId){
            logger.error("Endpoint: /api/experts/tickets/$ticketId/resolve Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        else if (!allowedStates.contains(ticket.state)) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/resolve Error: Invalid ticket status for this operation.")
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
            logger.error("Endpoint: /api/experts/tickets/$ticketId/close Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        val expert = ticket.expert
        if(expert == null){
            logger.error("Endpoint: /api/experts/tickets/$ticketId/close Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }
        val allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED)

        if (expert.id != expertId) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/close Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }
        else if (!allowedStates.contains(ticket.state)){
            logger.error("Endpoint: /api/experts/tickets/$ticketId/close Error: Invalid ticket status for this operation.")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation.")
        }

        ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
    }


    @PostMapping("/api/experts/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    fun sendTicketMessage(
        @ModelAttribute message: MessageObject,
        @PathVariable ticketId: Long
    ): MessageDTO {

        /* checking that experts exists */
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        val expert = expertService.getExpertById(expertId)
        expert ?: run {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        /* checking that ticket exists */
        val ticket = ticketService.getTicketModelById(ticketId)
        ticket ?: run {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        /* checking that expert exists in ticket */
        ticket.expert ?: run {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Expert not assigned to this ticket.")
            throw Exception.ExpertNotFoundException("Expert not assigned to this ticket.")
        }

        /* asserting ticket assignment */
        if (ticket.expert?.id != expertId) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Forbidden.")
            throw TicketException.TicketForbiddenException("Forbidden.")
        }

        /* asserting ticket state */
        val allowedStates = mutableSetOf(
            TicketState.IN_PROGRESS,
            TicketState.RESOLVED
        )
        if (!allowedStates.contains(ticket.state)) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Invalid ticket status for this operation..")
            throw TicketException.TicketInvalidOperationException("Invalid ticket status for this operation..")
        }

        /* retrieving sender username */
        val sender = securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.USERNAME)

        /* saving message in DB */
        return ticketService.sendTicketMessage(message, ticketId, sender)

    }


    @GetMapping("/api/experts/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketMessages(
        @PathVariable ticketId: Long,
        @RequestParam("pageNo", defaultValue = "1") pageNo: Int
    ): PageResponseDTO<MessageDTO> {

        /* checking that expert exists in DB */
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        val expert = expertService.getExpertById(expertId)
        expert ?: run {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Expert not found.")
            throw Exception.ExpertNotFoundException("Expert not found.")
        }

        /* checking that ticket exists */
        val ticket = ticketService.getTicketModelById(ticketId)
        ticket ?: run {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Ticket not found.")
            throw TicketException.TicketNotFoundException("Ticket not found.")
        }

        /* checking that expert exists in ticket */
        ticket.expert ?: run {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Expert not assigned to this ticket.")
            throw Exception.ExpertNotFoundException("Expert not assigned to this ticket.")
        }

        /* asserting ticket assignment */
        if (ticket.expert?.id != expertId) {
            logger.error("Endpoint: /api/experts/tickets/$ticketId/messages Error: Forbidden.")
            throw TicketException.TicketForbiddenException("Forbidden.")
        }

        /* fetching page from DB */
        var result: PageResponseDTO<MessageDTO> = PageResponseDTO()
        val sort: Sort = Sort.by("timestamp").descending()
        val page: Pageable = PageRequest.of(pageNo-1, result.computePageSize(), sort)  /* ticketing-service uses 1-based paged mechanism while Pageable uses 0-based paged mechanism*/

        /* return result to client */
        result = ticketService.getAllMessagesWithPagingByTicketId(ticketId, page).toDTO()
        return result

    }
}
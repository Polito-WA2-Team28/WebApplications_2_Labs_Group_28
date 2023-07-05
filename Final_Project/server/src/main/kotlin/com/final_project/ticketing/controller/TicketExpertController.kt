package com.final_project.ticketing.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.exception.Exception
import com.final_project.server.service.ExpertService
import com.final_project.ticketing.exception.TicketException
import com.final_project.ticketing.service.TicketService
import com.final_project.ticketing.util.TicketState


import com.final_project.ticketing.dto.*
import com.final_project.ticketing.dto.PageResponseDTO.Companion.toDTO
import com.final_project.ticketing.util.Nexus
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
    fun getTickets(
        @RequestParam("pageNo", defaultValue = "1") pageNo: Int
    ): PageResponseDTO<TicketDTO> {

        val nexus: Nexus = Nexus(expertService, ticketService)

        /* running checks... */
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/experts/tickets")
            .assertExpertExists(expertId)

        /* crafting pageable request */
        var result: PageResponseDTO<TicketDTO> = PageResponseDTO()
        val page: Pageable = PageRequest.of(pageNo-1, result.computePageSize())

        result = ticketService.getAllTicketsWithPagingByExpertId(expertId, page).toDTO()
        return result

    }

    @GetMapping("/api/experts/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(expertService, ticketService)

        /* running checks... */
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/experts/tickets/$ticketId")
            .assertExpertExists(expertId)
            .assertTicketExists(ticketId)
            .assertTicketAssignment()

        return nexus.ticket
    }

    @PatchMapping("/api/experts/tickets/{ticketId}/resolve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resolveTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(expertService, ticketService)

        /* running checks... */
        val allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED, TicketState.IN_PROGRESS)
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/experts/tickets/$ticketId/resolve")
            .assertExpertExists(expertId)
            .assertTicketExists(ticketId)
            .assertTicketAssignment()
            .assertTicketStatus(allowedStates)
            .resolveTicket(ticketId)

        return nexus.ticket
    }

    @PatchMapping("/api/experts/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(expertService, ticketService)

        /* running checks... */
        val allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED)
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/experts/tickets/$ticketId/close")
            .assertExpertExists(expertId)
            .assertTicketExists(ticketId)
            .assertTicketAssignment()
            .assertTicketStatus(allowedStates)
            .closeTicket(ticketId)

        return nexus.ticket
    }

    @PostMapping("/api/experts/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    fun sendTicketMessage(
        @ModelAttribute message: MessageObject,
        @PathVariable ticketId: Long
    ): MessageDTO {

        val nexus: Nexus = Nexus(expertService, ticketService)

        /* running checks... */
        val allowedStates = mutableSetOf(TicketState.IN_PROGRESS, TicketState.RESOLVED)
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/experts/tickets/$ticketId/close")
            .assertExpertExists(expertId)
            .assertTicketExists(ticketId)
            .assertTicketAssignment()
            .assertTicketStatus(allowedStates)

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

        val nexus: Nexus = Nexus(expertService, ticketService)

        /* running checks... */
        val expertId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/experts/tickets/$ticketId/close")
            .assertExpertExists(expertId)
            .assertTicketExists(ticketId)
            .assertTicketAssignment()

        /* fetching page from DB */
        var result: PageResponseDTO<MessageDTO> = PageResponseDTO()
        val sort: Sort = Sort.by("timestamp").descending()
        val page: Pageable = PageRequest.of(pageNo-1, result.computePageSize(), sort)  /* ticketing-service uses 1-based paged mechanism while Pageable uses 0-based paged mechanism*/

        /* return result to client */
        result = ticketService.getAllMessagesWithPagingByTicketId(ticketId, page).toDTO()
        return result

    }
}
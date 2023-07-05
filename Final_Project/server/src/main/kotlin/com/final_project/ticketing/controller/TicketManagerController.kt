package com.final_project.ticketing.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.exception.Exception
import com.final_project.server.model.Expert
import com.final_project.server.service.ExpertService
import com.final_project.server.service.ManagerService
import com.final_project.ticketing.dto.*
import com.final_project.ticketing.dto.PageResponseDTO.Companion.toDTO
import com.final_project.ticketing.service.TicketService
import com.final_project.ticketing.util.Nexus
import com.final_project.ticketing.util.TicketState
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    fun getTickets(
        @RequestParam("pageNo", defaultValue = "1") pageNo: Int
    ): PageResponseDTO<TicketDTO> {

        val nexus: Nexus = Nexus(managerService, expertService, ticketService)

        /* running checks... */
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/managers/tickets")
            .assertManagerExists(managerId)

        /* crafting pageable request */
        var result: PageResponseDTO<TicketDTO> = PageResponseDTO()
        var page: Pageable = PageRequest.of(pageNo-1, result.computePageSize())

        /* return result to client */
        result = ticketService.getAllTicketsWithPaging(page).toDTO()
        return result
    }

    @GetMapping("/api/managers/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(managerService, expertService, ticketService)

        /* running checks... */
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/managers/tickets/$ticketId")
            .assertManagerExists(managerId)
            .assertTicketExists(ticketId)

        return nexus.ticket
    }

    @PatchMapping("/api/managers/tickets/{ticketId}/assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun assignTicket (
        @PathVariable("ticketId") ticketId: Long,
        @RequestBody @Valid ticketUpdateData: TicketUpdateData
    ): TicketDTO? {

        val nexus: Nexus = Nexus(managerService, expertService, ticketService)

        /* running checks... */
        val allowedStates = mutableSetOf(TicketState.OPEN)
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/managers/tickets/$ticketId/assign")
            .assertManagerExists(managerId)
            .assertTicketExists(ticketId)
            .assertTicketStatus(allowedStates)
            .assertExpertExists(ticketUpdateData.expertId)
            .assignTicketToExpert(ticketId, ticketUpdateData.expertId)

        return nexus.ticket
    }

    @PatchMapping("/api/managers/tickets/{ticketId}/relieveExpert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun relieveExpert (
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(managerService, expertService, ticketService)

        /* running checks... */
        val allowedStates = mutableSetOf(TicketState.IN_PROGRESS)
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/managers/tickets/$ticketId/relieveExpert")
            .assertManagerExists(managerId)
            .assertTicketExists(ticketId)
            .assertTicketStatus(allowedStates)
            .relieveExpertFromTicket(ticketId)

        return nexus.ticket
    }

    @PatchMapping("/api/managers/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket (
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(managerService, expertService, ticketService)

        /* running checks... */
        val allowedStates = mutableSetOf(TicketState.OPEN, TicketState.RESOLVED, TicketState.REOPENED)
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/managers/tickets/$ticketId/close")
            .assertManagerExists(managerId)
            .assertTicketExists(ticketId)
            .assertTicketStatus(allowedStates)
            .closeTicket(ticketId)

        return nexus.ticket
    }

    @PatchMapping("/api/managers/tickets/{ticketId}/resumeProgress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resumeTicketProgress (
        @PathVariable("ticketId") ticketId: Long,
        @RequestBody @Valid ticketUpdateData: TicketUpdateData
    ): TicketDTO? {

        val nexus: Nexus = Nexus(managerService, expertService, ticketService)

        /* running checks... */
        val allowedStates = mutableSetOf(TicketState.REOPENED)
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/managers/tickets/$ticketId/resumeProgress")
            .assertManagerExists(managerId)
            .assertTicketExists(ticketId)
            .assertExpertExists(ticketUpdateData.expertId)
            .assertTicketStatus(allowedStates)
            .assignTicketToExpert(ticketId, ticketUpdateData.expertId)

        return nexus.ticket
    }

    @DeleteMapping("/api/managers/tickets/{ticketId}/remove")
    @ResponseStatus(HttpStatus.OK)
    fun removeTicket(
        @PathVariable("ticketId") ticketId: Long
    ) {

        val nexus: Nexus = Nexus(managerService, expertService, ticketService)

        /* running checks... */
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/managers/tickets/$ticketId/resumeProgress")
            .assertManagerExists(managerId)
            .assertTicketExists(ticketId)

        /* removing the ticket from the database */
        ticketService.removeTicketById(ticketId)
    }


    @GetMapping("/api/managers/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketMessages(
        @PathVariable ticketId: Long,
        @RequestParam("pageNo", defaultValue = "1") pageNo: Int
    ): PageResponseDTO<MessageDTO> {

        val nexus: Nexus = Nexus(managerService, expertService, ticketService)

        /* running checks... */
        val managerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/managers/tickets/$ticketId/resumeProgress")
            .assertManagerExists(managerId)
            .assertTicketExists(ticketId)

        /* fetching page from DB */
        var result: PageResponseDTO<MessageDTO> = PageResponseDTO()
        val sort: Sort = Sort.by("timestamp").descending()
        val page: Pageable = PageRequest.of(pageNo-1, result.computePageSize(), sort)  /* ticketing-service uses 1-based paged mechanism while Pageable uses 0-based paged mechanism*/

        /* return result to client */
        result = ticketService.getAllMessagesWithPagingByTicketId(ticketId, page).toDTO()
        return result

    }
}
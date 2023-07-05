package com.final_project.ticketing.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.model.*
import com.final_project.server.service.*
import com.final_project.ticketing.dto.*
import com.final_project.ticketing.service.TicketService
import com.final_project.ticketing.util.TicketState
import com.final_project.ticketing.dto.PageResponseDTO.Companion.toDTO
import com.final_project.ticketing.util.Nexus
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Observed
class TicketCustomerController @Autowired constructor(
    val ticketService: TicketService,
    val customerService: CustomerService,
    val productService: ProductService,
    val securityConfig: SecurityConfig,
    val fileStorageService: FileStorageService
) {

    val logger: Logger = LoggerFactory.getLogger(TicketCustomerController::class.java)

    @PostMapping("/api/customers/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTicket(
        @RequestBody @Valid ticket: TicketCreationData,
        br: BindingResult
    ): TicketDTO {

        val nexus: Nexus = Nexus(customerService, ticketService, productService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/customers/tickets")
            .assertCustomerExists(customerId)
            .assertProductExists(ticket.serialNumber)
            .assertProductOwnership()

        /** TODO: Check "!!" safety*/
        /* creating ticket */
        val createdTicket = ticketService.createTicket(ticket, nexus.customer!!, nexus.product!!)
        nexus
            .assertTicketNonNull(createdTicket)
            .assertTicketExists(createdTicket!!.ticketId!!)

        return createdTicket
    }

    @GetMapping("/api/customers/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(
        @RequestParam("pageNo", defaultValue = "1") pageNo: Int
    ): PageResponseDTO<TicketDTO> {

        val nexus: Nexus = Nexus(customerService, ticketService, productService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/customers/tickets")
            .assertCustomerExists(customerId)

        /* crafting pageable request */
        var result: PageResponseDTO<TicketDTO> = PageResponseDTO()
        val page: Pageable = PageRequest.of(pageNo-1, result.computePageSize())

        /* return result to client */
        result = ticketService.getAllTicketsWithPagingByCustomerId(customerId, page).toDTO()
        return result
    }

    @GetMapping("/api/customers/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(customerService, ticketService, productService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus.setEndpointForLogger("/api/customers/tickets/$ticketId")
            .assertCustomerExists(customerId)
            .assertTicketExists(ticketId)
            .assertTicketOwnership()

        return nexus.ticket
    }

    @PatchMapping("/api/customers/tickets/{ticketId}/reopen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reopenTicket(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(customerService, ticketService, productService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        val allowedStates = mutableSetOf(TicketState.CLOSED, TicketState.RESOLVED)
        nexus
            .setEndpointForLogger("/api/customers/tickets/$ticketId/reopen")
            .assertCustomerExists(customerId)
            .assertTicketExists(ticketId)
            .assertTicketOwnership()
            .assertTicketStatus(allowedStates)
            .reopenTicket(ticketId)

        return nexus.ticket
    }

    @PatchMapping("/api/customers/tickets/{ticketId}/compileSurvey")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun compileTicketSurvey(
        @PathVariable("ticketId") ticketId: Long
    ): TicketDTO? {

        val nexus: Nexus = Nexus(customerService, ticketService, productService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        val allowedStates = mutableSetOf(TicketState.RESOLVED)
        nexus
            .setEndpointForLogger("/api/customers/tickets/$ticketId/compileSurvey")
            .assertCustomerExists(customerId)
            .assertTicketExists(ticketId)
            .assertTicketOwnership()
            .assertTicketStatus(allowedStates)
            .closeTicket(ticketId)

        return nexus.ticket
    }


    @PostMapping("/api/customers/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    fun sendTicketMessage(
        @ModelAttribute message:MessageObject,
        @PathVariable ticketId: Long
    ): MessageDTO {

        val nexus: Nexus = Nexus(customerService, ticketService, productService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        val allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED, TicketState.IN_PROGRESS, TicketState.RESOLVED)
        nexus
            .setEndpointForLogger("/api/customers/tickets/$ticketId/messages")
            .assertCustomerExists(customerId)
            .assertTicketExists(ticketId)
            .assertTicketOwnership()
            .assertTicketStatus(allowedStates)

        /* retrieving sender username */
        val sender = securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.USERNAME)

        /* saving message in DB */
        return ticketService.sendTicketMessage(message, ticketId, sender)

    }


    @GetMapping("/api/customers/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketMessages(
        @PathVariable ticketId: Long,
        @RequestParam("pageNo", defaultValue = "1") pageNo: Int
    ): PageResponseDTO<MessageDTO> {

        val nexus: Nexus = Nexus(customerService, ticketService, productService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/customers/tickets/$ticketId/messages")
            .assertCustomerExists(customerId)
            .assertTicketExists(ticketId)
            .assertTicketOwnership()

        /* crafting pageable request */
        var result: PageResponseDTO<MessageDTO> = PageResponseDTO()
        val sort: Sort = Sort.by("timestamp").descending()
        val page: Pageable = PageRequest.of(pageNo-1, result.computePageSize(), sort)  /* ticketing-service uses 1-based paged mechanism while Pageable uses 0-based paged mechanism*/

        /* return result to client */
        result = ticketService.getAllMessagesWithPagingByTicketId(ticketId, page).toDTO()
        return result

    }

    @GetMapping("/api/customers/tickets/{ticketId}/attachments/{attachmentUniqueName}")
    @ResponseStatus(HttpStatus.OK)
    fun getMessageAttachment(
        @PathVariable attachmentUniqueName: String,
        @PathVariable ticketId: Long
    ): ResponseEntity<ByteArray> {

        val nexus: Nexus = Nexus(customerService, ticketService, productService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/customers/tickets/$ticketId/attachments/$attachmentUniqueName")
            .assertCustomerExists(customerId)
            .assertTicketExists(ticketId)
            .assertTicketOwnership()
            //.assertFileExists() /* TODO: need to assert that the uniqueFileName corresponds to a file that exists */

        return fileStorageService.getAttachmentFile(attachmentUniqueName)
    }

}
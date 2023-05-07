package com.lab3.ticketing.controller

import com.lab3.server.exception.Exception
import com.lab3.server.service.ExpertService
import com.lab3.ticketing.dto.TicketDTO
import com.lab3.ticketing.service.TicketService
import com.lab3.ticketing.util.TicketState
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

    @GetMapping("/API/experts/{expertId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("expertId") expertId:Long,
                        @PathVariable("ticketId") ticketId:Long): TicketDTO?{
        return ticketService.getTicketDTOById(ticketId) //Add exception handling like in product controller
    }

    @PatchMapping("/API/experts/{expertId}/tickets/{ticketId}/resolve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resolveTicket(@PathVariable("expertId") expertId:Long,
                     @PathVariable("ticketId") ticketId:Long){

        var ticket = ticketService.getTicketModelById(ticketId)
        var expert = ticket?.expert
        var allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED, TicketState.IN_PROGRESS)

        if(ticket != null &&
           expert != null &&
           expert.getId() == expertId &&
           allowedStates.contains(ticket.state)){

            ticketService.changeTicketStatus(ticket, TicketState.RESOLVED)
        }



    }

    @PatchMapping("/API/experts/{expertId}/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket(@PathVariable("expertId") expertId:Long,
                     @PathVariable("ticketId") ticketId:Long){

        var ticket = ticketService.getTicketModelById(ticketId)
        var expert = ticket?.expert
        var allowedStates = mutableSetOf(TicketState.OPEN, TicketState.REOPENED)

        if(ticket != null &&
           expert != null &&
           expert.getId() == expertId &&
           allowedStates.contains(ticket.state)){

            ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
        }
    }
}
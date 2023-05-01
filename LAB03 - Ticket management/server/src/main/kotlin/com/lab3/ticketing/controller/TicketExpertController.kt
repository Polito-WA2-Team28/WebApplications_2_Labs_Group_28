package com.lab3.ticketing.controller

import com.lab3.ticketing.service.TicketCustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketExpertController @Autowired constructor(val ticketService: TicketCustomerService) {

    @GetMapping("/API/experts/{expertId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@PathVariable("expertId") expertId:Long){

    }

    @GetMapping("/API/experts/{expertId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("expertId") expertId:Long,
                        @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/experts/{expertId}/tickets/{ticketId}/resolve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resolveTicket(@PathVariable("expertId") expertId:Long,
                     @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/experts/{expertId}/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket(@PathVariable("expertId") expertId:Long,
                     @PathVariable("ticketId") ticketId:Long){

    }
}
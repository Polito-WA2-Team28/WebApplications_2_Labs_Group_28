package com.lab3.ticketing.controller

import com.lab3.ticketing.service.TicketCustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketCustomerController @Autowired constructor(val ticketService: TicketCustomerService) {

    @PostMapping("/API/customers/{customerId}/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTicket(@PathVariable("customerId") customerId:Long){

    }

    @GetMapping("/API/customers/{customerId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@PathVariable("customerId") customerId:Long){

    }

    @GetMapping("/API/customers/{customerId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("customerId") customerId:Long,
                        @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/customers/{customerId}/tickets/{ticketId}/reopen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reopenTicket(@PathVariable("customerId") customerId:Long,
                        @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/customers/{customerId}/tickets/{ticketId}/compileSurvey")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun compileTicketSurvey(@PathVariable("customerId") customerId:Long,
                     @PathVariable("ticketId") ticketId:Long){

    }
}
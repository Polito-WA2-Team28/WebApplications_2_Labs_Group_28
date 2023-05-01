package com.lab3.ticketing.controller

import com.lab3.ticketing.service.TicketCustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketManagerController @Autowired constructor(val ticketService: TicketCustomerService) {
    @GetMapping("/API/managers/{managerId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@PathVariable("managerId") managerId:Long){

    }

    @GetMapping("/API/managers/{managerId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("managerId") managerId:Long,
                        @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun assignTicket(@PathVariable("managerId") managerId:Long,
                     @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/relieveExpert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun relieveExpert(@PathVariable("managerId") managerId:Long,
                      @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun closeTicket(@PathVariable("managerId") managerId:Long,
                    @PathVariable("ticketId") ticketId:Long){

    }

    @PatchMapping("/API/managers/{managerId}/tickets/{ticketId}/resumeProgress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resumeTicketProgress(@PathVariable("managerId") managerId:Long,
                             @PathVariable("ticketId") ticketId:Long){

    }

    @DeleteMapping("/API/managers/{managerId}/tickets/{ticketId}/remove")
    @ResponseStatus(HttpStatus.OK)
    fun removeTicket(@PathVariable("managerId") managerId:Long,
                     @PathVariable("ticketId") ticketId:Long){

    }
}
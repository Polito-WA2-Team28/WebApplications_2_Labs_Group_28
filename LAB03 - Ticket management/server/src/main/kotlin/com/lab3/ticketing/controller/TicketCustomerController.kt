package com.lab3.ticketing.controller

import com.lab3.server.dto.CustomerDTO
import com.lab3.server.dto.CustomerFormRegistration
import com.lab3.server.dto.ProductDTO
import com.lab3.server.model.Customer
import com.lab3.server.model.Product
import com.lab3.server.service.CustomerServiceImpl
import com.lab3.server.service.ProductServiceImpl
import com.lab3.ticketing.dto.TicketCreationData
import com.lab3.ticketing.dto.TicketDTO
import com.lab3.ticketing.service.TicketServiceImpl
import com.lab3.ticketing.util.TicketState
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
class TicketCustomerController @Autowired constructor(
    val ticketService: TicketServiceImpl,
    val customerService: CustomerServiceImpl,
    val productService: ProductServiceImpl
) {

    @PostMapping("/API/customers/{customerId}/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTicket(@PathVariable("customerId") customerId:Long,
                     @RequestBody @Valid ticket: TicketCreationData,
                     br: BindingResult){

        var customer: Customer? = customerService.getCustomerById(customerId)
        var product: Product? = productService.getProductBySerialNumber(ticket.serialNumber)


        if (customer != null && product != null && product.owner == customer) {
            ticketService.createTicket(ticket, customer, product)
        }
        else{
            //throw exception
        }
    }

    @GetMapping("/API/customers/{customerId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@PathVariable("customerId") customerId:Long): List<TicketDTO>{
        return ticketService.getAllCustomerTickets(customerId)
    }

    @GetMapping("/API/customers/{customerId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("customerId") customerId:Long,
                        @PathVariable("ticketId") ticketId:Long): TicketDTO?{
        var ticket = ticketService.getTicketDTOById(ticketId)

        if(ticket != null && ticket.customerId == customerId){
            return ticket
        }
        else{
            // not allowed
            return null
        }
    }

    @PatchMapping("/API/customers/{customerId}/tickets/{ticketId}/reopen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reopenTicket(@PathVariable("customerId") customerId:Long,
                     @PathVariable("ticketId") ticketId:Long):TicketDTO? {
        var ticket = ticketService.getTicketModelById(ticketId)
        var allowedStates = mutableSetOf(TicketState.CLOSED, TicketState.RESOLVED)

        return if(ticket != null &&
                  ticket.customer.getId() == customerId &&
                  allowedStates.contains(ticket.state)){

                  ticketService.changeTicketStatus(ticket, TicketState.REOPENED)
        } else{
            null
        }
    }

    @PatchMapping("/API/customers/{customerId}/tickets/{ticketId}/compileSurvey")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun compileTicketSurvey(@PathVariable("customerId") customerId:Long,
                     @PathVariable("ticketId") ticketId:Long):TicketDTO?{

        var ticket = ticketService.getTicketModelById(ticketId)

        return if(ticket != null &&
                  ticket.customer.getId() == customerId &&
                  ticket.state == TicketState.RESOLVED){

            ticketService.changeTicketStatus(ticket, TicketState.CLOSED)
        } else {
            null
        }
    }
}
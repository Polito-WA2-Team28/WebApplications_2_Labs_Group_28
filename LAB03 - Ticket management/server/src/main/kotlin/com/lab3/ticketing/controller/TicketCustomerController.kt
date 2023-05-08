package com.lab3.ticketing.controller

import com.lab3.server.exception.Exception
import com.lab3.server.model.Customer
import com.lab3.server.model.Product
import com.lab3.server.service.CustomerService
import com.lab3.server.service.ProductService
import com.lab3.ticketing.dto.TicketCreationData
import com.lab3.ticketing.dto.TicketDTO
import com.lab3.ticketing.service.TicketService
import com.lab3.ticketing.util.TicketState
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*


@RestController
class TicketCustomerController @Autowired constructor(
    val ticketService: TicketService,
    val customerService: CustomerService,
    val productService: ProductService
) {

    @PostMapping("/api/customers/{customerId}/tickets")
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

    @GetMapping("/api/customers/{customerId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(
        @PathVariable("customerId") customerId: Long,
        @RequestParam("pageNo", defaultValue = "0") pageNo: Int
    ): Page<TicketDTO> {

        /* checking that customer exists */
        customerService.getCustomerById(customerId)
            ?: throw Exception.CustomerNotFoundException("Customer not found.")

        /* computing page and retrieving all the tickets corresponding to this customer */
        var page: Pageable = PageRequest.of(pageNo, 3)
        return ticketService.getAllTicketsWithPagingByCustomerId(customerId, page)
    }

    @GetMapping("/api/customers/{customerId}/tickets/{ticketId}")
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

    @PatchMapping("/api/customers/{customerId}/tickets/{ticketId}/reopen")
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

    @PatchMapping("/api/customers/{customerId}/tickets/{ticketId}/compileSurvey")
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
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
            println("All checked")
            ticketService.createTicket(ticket, customer, product)
        }
        else{
            println("nope")
        }
    }

    @GetMapping("/API/customers/{customerId}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTickets(@PathVariable("customerId") customerId:Long){

    }

    @GetMapping("/API/customers/{customerId}/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSingleTicket(@PathVariable("customerId") customerId:Long,
                        @PathVariable("ticketId") ticketId:Long): TicketDTO?{
        return ticketService.getTicketById(ticketId)
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
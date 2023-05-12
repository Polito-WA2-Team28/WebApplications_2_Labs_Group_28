package com.lab4.ticketing.dto

import com.lab4.ticketing.model.Ticket
import com.lab4.ticketing.util.TicketState
import java.util.Date

data class TicketDTO(val ticketId:Long?, val ticketState:TicketState, val description:String, val serialNumber:Long,
                     val customerId:Long?, val expertId:Long?, val creationDate:Date, val lastModified:Date) {
}


fun Ticket.toDTO() : TicketDTO {
    return TicketDTO(this.getId(), state, description, this.product.serialNumber,
                     this.customer.getId(), this.expert?.getId(), this.creationDate, this.lastModified)
}
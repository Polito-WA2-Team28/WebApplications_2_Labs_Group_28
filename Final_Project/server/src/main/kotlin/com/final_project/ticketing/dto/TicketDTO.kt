package com.final_project.ticketing.dto

import com.final_project.ticketing.model.Ticket
import com.final_project.ticketing.util.TicketState
import java.util.Date
import java.util.UUID

data class TicketDTO(
    val ticketId: Long?,
    var ticketState: TicketState,
    val description: String,
    val serialNumber: UUID,
    val customerId: UUID?,
    var expertId: UUID?,
    val creationDate: Date,
    val lastModified:Date
) {

    fun assignExpert(expertId: UUID?) {
        this.expertId = expertId
    }

    fun relieveExpert() {
        this.expertId = null
    }

    fun changeState(newState: TicketState) {
        this.ticketState = newState
    }
}


fun Ticket.toDTO() : TicketDTO {
    return TicketDTO(this.getId(), state, description, this.product.serialNumber,
                     this.customer.id, this.expert?.id, this.creationDate, this.lastModified)
}
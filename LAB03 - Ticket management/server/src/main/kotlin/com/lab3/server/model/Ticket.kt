package com.lab3.server.model

import com.lab3.server.util.TicketState
import com.lab3.server.model.Profile
import jakarta.persistence.*

@Entity
@Table
class Ticket(
    var state:TicketState,
    @ManyToOne var customer:Profile,
    @ManyToOne var expert:Profile,
    var description:String,
    @ManyToOne var product:Product,
    @OneToMany var messages: MutableSet<Message>

    ): EntityBase<Long>() {

}
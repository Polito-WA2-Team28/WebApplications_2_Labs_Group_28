package com.lab3.ticketing.model



import com.lab3.ticketing.util.TicketState
import jakarta.persistence.*


@Entity
@Table
class Ticket(
    var state:TicketState,
    //@ManyToOne var customer:Profile,
    //@ManyToOne var expert:Profile,
    var description:String,
    //@ManyToOne var product:Product,
    @OneToMany var messages: MutableSet<Message>

): EntityBase<Long>() {

}
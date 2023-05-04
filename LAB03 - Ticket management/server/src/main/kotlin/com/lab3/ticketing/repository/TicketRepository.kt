package com.lab3.ticketing.repository

import com.lab3.server.model.Customer
import com.lab3.server.model.Product
import com.lab3.ticketing.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : CrudRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.expert.id = :expertId")
    fun findByExpertId(expertId:Long): List<Ticket>

    @Query("SELECT t FROM Ticket t WHERE t.customer.id = :customerId")
    fun findByCustomerId(customerId:Long): List<Ticket>
}
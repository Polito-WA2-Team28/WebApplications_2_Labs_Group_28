package com.lab4.ticketing.repository

import com.lab4.ticketing.model.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : CrudRepository<Ticket, Long>, JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.expert.id = :expertId")
    fun findByExpertId(expertId:Long): List<Ticket>

    @Query("SELECT t FROM Ticket t WHERE t.customer.id = :customerId")
    fun findByCustomerId(customerId: Long): List<Ticket>

    fun findAllByCustomerId(customerId: Long, pageable: Pageable): Page<Ticket>

    fun findAllByExpertId(expertId: Long, pageable: Pageable): Page<Ticket>
}
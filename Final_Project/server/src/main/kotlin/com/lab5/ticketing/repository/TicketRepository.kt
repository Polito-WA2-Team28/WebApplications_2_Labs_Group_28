package com.lab5.ticketing.repository

import com.lab5.ticketing.model.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TicketRepository : CrudRepository<Ticket, Long>, JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.expert.id = :expertId")
    fun findByExpertId(expertId:UUID): List<Ticket>

    @Query("SELECT t FROM Ticket t WHERE t.customer.id = :customerId")
    fun findByCustomerId(customerId: UUID): List<Ticket>

    fun findAllByCustomerId(customerId: UUID, pageable: Pageable): Page<Ticket>

    fun findAllByExpertId(expertId: UUID, pageable: Pageable): Page<Ticket>
}
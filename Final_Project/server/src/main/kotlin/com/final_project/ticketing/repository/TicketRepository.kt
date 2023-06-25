package com.final_project.ticketing.repository

import com.final_project.ticketing.model.Ticket
import com.final_project.ticketing.util.TicketState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
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

    @Query("UPDATE Ticket t SET t.state = :newState WHERE t.id = :ticketId")
    @Modifying
    fun updateTicketState(ticketId: Long, newState: TicketState)

    @Query("UPDATE Ticket t SET t.expert.id = :expertId WHERE t.id = :ticketId")
    @Modifying
    fun assignTicketToExpert(ticketId: Long, expertId: UUID)

    @Query("UPDATE Ticket t SET t.expert.id = null WHERE t.id = :ticketId")
    @Modifying
    fun relieveExpertFromTicket(ticketId: Long)

    fun findAllByCustomerId(customerId: UUID, pageable: Pageable): Page<Ticket>

    fun findAllByExpertId(expertId: UUID, pageable: Pageable): Page<Ticket>

}
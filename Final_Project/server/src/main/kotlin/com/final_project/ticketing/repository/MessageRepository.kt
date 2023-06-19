package com.final_project.ticketing.repository

import com.final_project.ticketing.dto.MessageObject
import com.final_project.ticketing.model.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import java.util.*

interface MessageRepository: CrudRepository<Message, Long>, JpaRepository<Message, Long> {

    fun findAllByTicketId(
        ticketId: Long,
        pageable: Pageable
    ): Page<Message>
}
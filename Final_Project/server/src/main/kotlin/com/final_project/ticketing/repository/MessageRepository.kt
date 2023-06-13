package com.final_project.ticketing.repository

import com.final_project.ticketing.model.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface MessageRepository: CrudRepository<Message, Long>, JpaRepository<Message, Long> {
}
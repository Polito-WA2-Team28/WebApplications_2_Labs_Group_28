package com.lab5.ticketing.repository

import com.lab5.ticketing.model.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface MessageRepository: CrudRepository<Message, Long>, JpaRepository<Message, Long> {
}
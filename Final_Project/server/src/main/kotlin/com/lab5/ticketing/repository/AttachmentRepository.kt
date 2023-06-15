package com.lab5.ticketing.repository

import com.lab5.ticketing.model.Attachment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttachmentRepository : JpaRepository<Attachment, Int>
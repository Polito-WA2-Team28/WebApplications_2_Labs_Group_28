package com.lab4.server.service

import com.lab4.server.model.Expert

interface ExpertService {

    fun getExpertById(id: Long): Expert?
}
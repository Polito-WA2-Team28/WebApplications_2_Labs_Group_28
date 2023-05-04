package com.lab3.server.service

import com.lab3.server.model.Expert

interface ExpertService {

    fun getExpertById(id: Long): Expert?
}
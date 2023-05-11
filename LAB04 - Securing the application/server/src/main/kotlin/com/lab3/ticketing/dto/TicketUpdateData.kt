package com.lab3.ticketing.dto

import jakarta.validation.constraints.NotNull

class TicketUpdateData (

    @field:NotNull
    var expertId: Long
) {}
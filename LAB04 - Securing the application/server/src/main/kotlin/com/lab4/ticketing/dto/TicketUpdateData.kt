package com.lab4.ticketing.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

class TicketUpdateData (

    @field:NotNull
    var expertId: UUID
) {}
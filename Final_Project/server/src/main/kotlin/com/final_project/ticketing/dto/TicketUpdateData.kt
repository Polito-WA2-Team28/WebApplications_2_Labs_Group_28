package com.final_project.ticketing.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

class TicketUpdateData (

    @field:NotNull
    var expertId: UUID
) {}
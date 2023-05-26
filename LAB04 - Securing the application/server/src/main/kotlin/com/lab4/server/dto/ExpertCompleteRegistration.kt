package com.lab4.server.dto

import com.lab4.ticketing.util.ExpertiseFieldEnum
import java.util.*

class ExpertCompleteRegistration(
    var id: UUID,
    val email:String,
    val expertiseFields: MutableSet<ExpertiseFieldEnum>
) {

}

fun ExpertFormRegistration.toCompleteExpert(id: UUID, profile:ExpertFormRegistration): ExpertCompleteRegistration {
    return ExpertCompleteRegistration(id, email, expertiseFields)
}
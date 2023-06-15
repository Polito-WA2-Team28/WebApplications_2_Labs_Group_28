package com.lab5.server.dto

import com.lab5.ticketing.util.ExpertiseFieldEnum
import java.util.*

class ExpertCompleteRegistration (
    val id: UUID,
    val email: String,
    var expertiseFields: MutableSet<ExpertiseFieldEnum>
) {

}

fun ExpertFormRegistration.toCompleteExpert(
    id: UUID,
    profile: ExpertFormRegistration,
): ExpertCompleteRegistration {
    return ExpertCompleteRegistration(id, profile.email, profile.expertiseFields)
}
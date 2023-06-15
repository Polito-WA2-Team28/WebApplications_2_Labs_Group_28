package com.final_project.server.model

import com.final_project.server.dto.ExpertCompleteRegistration
import com.final_project.ticketing.util.ExpertiseFieldEnum
import com.final_project.server.dto.CustomerCompleteRegistration
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table
class Expert(
    @Id val id: UUID,
    var email:String,

    @ElementCollection(targetClass = ExpertiseFieldEnum::class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="expertise_fields")
    @Column(name="field")
    var expertiseFields: MutableSet<ExpertiseFieldEnum>
)

/**
 * Create a (model) Expert object given the Expert information retrieved from the Expert registration form.
 *
 * @return a Expert object.
 */
fun ExpertCompleteRegistration.toModel(): Expert {
    return Expert(id, email, expertiseFields)
}
package com.lab5.server.model

import com.lab5.server.dto.CustomerCompleteRegistration
import com.lab5.server.dto.ExpertCompleteRegistration
import com.lab5.ticketing.util.ExpertiseFieldEnum
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
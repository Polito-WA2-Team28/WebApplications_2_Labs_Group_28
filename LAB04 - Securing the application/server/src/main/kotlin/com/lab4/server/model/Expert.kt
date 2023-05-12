package com.lab4.server.model

import com.lab4.ticketing.util.ExpertiseFieldEnum
import jakarta.persistence.*

@Entity
@Table
class Expert(
    var email:String,

    @ElementCollection(targetClass = ExpertiseFieldEnum::class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="expertise_fields")
    @Column(name="field")
    var expertiseFields:MutableSet<ExpertiseFieldEnum>
): EntityBase<Long>() {
}
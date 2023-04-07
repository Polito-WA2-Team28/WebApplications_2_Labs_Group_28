package com.lab2.server.model


import com.lab2.server.dto.ProfileForm
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name="profile")
class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="profile_id")
    var id:Int = 0

    var name:String
    var surname:String
    var registrationDate:Date
    var birthDate:Date
    var email:String
    var phoneNumber:String

    constructor(id:Int, name:String, surname:String, registrationDate:Date, birthDate:Date, email:String, phoneNumber:String){
        this.id = id
        this.name = name
        this.surname = surname
        this.registrationDate = registrationDate
        this.birthDate = birthDate
        this.email = email
        this.phoneNumber = phoneNumber
    }


    /**
     * Overriding the "==" operator to compare two profiles. This has been first used in the
     * editProfile() method of the ProfileServiceImpl in order to check that the returned object
     * stored in the database is equal to th eobject provided by the user.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Profile) return false

        /* Comparing properties for equality. */
        return this.id == other.id &&
                this.name == other.name &&
                this.surname == other.surname &&
                this.registrationDate == other.registrationDate &&
                this.birthDate == other.birthDate &&
                this.email == other.email &&
                this.phoneNumber == other.phoneNumber
    }

}

fun ProfileForm.toModel(): Profile? {
    return if (registrationDate != null && email != null)
        Profile(0, name, surname, registrationDate, birthDate, email, phoneNumber)
    else
        null
}

/**
 * Create a (model) Profile object given the Profile information retrieved from the Profile modification form.
 * It receives in input the additional information the user could not modify.
 *
 * @param id the identifier of the user retrieved from the database.
 * @param registrationDate the registration date of the user retrieved from the database.
 * @param email the email of the user retrieved from the database.
 * @return a Profile object.
 */
fun ProfileForm.toModel(id: Int, registrationDate: Date, email: String): Profile {
    return Profile(id, name, surname, registrationDate, birthDate, email, phoneNumber)
}
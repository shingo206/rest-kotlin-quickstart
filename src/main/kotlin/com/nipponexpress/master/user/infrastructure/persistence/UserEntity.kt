package com.nipponexpress.master.user.infrastructure.persistence

import io.quarkus.hibernate.reactive.panache.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Column(nullable = false, unique = true)
    @field:NotBlank(message = "Name cannot be blank")
    @field:Size(min = 6, max = 50)
    var name: String,

    @Column(nullable = false, unique = true)
    @field:NotBlank(message = "E-mail cannot be blank")
    @field:Size(min = 6, max = 100)
    var email: String,

    @Column(nullable = false)
    @field:Min(16, message = "Age must be between 16 and 65")
    @field:Max(65, message = "Age must be between 16 and 65")
    var age: Int = 0,

    @Column(nullable = false)
    var birthDate: LocalDate,

    @Column(nullable = false)
    var active: Boolean = false,

    @Column(nullable = false)
    var createdAt: LocalDateTime,

    @Column(nullable = false)
    var updatedAt: LocalDateTime?,

    ) : PanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UserEntity

        if (id == null || other.id == null) return false

        return id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0

    override fun toString(): String = this::class.simpleName + "( id = $id, name=$name, email = $email )"
}

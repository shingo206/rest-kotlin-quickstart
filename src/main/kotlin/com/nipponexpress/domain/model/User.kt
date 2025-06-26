package com.nipponexpress.domain.model

import io.quarkus.hibernate.reactive.panache.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
data class User(
    @Column(nullable = false)
    @NotBlank(message = "Name cannot be blank")
    @field:Size(min = 6, max = 50)
    val name: String,
) : PanacheEntity() {}

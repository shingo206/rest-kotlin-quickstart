package com.nipponexpress.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    var id: Long? = 0,

    var name: String = "",

    var email: String = "",

    var age: Int = 0,

    var birthDate: LocalDate = LocalDate.now(),

    var active: Boolean = false,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime? = LocalDateTime.now(),
)


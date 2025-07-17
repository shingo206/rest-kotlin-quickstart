package com.nipponexpress.application.dto

import java.time.LocalDate

data class CreateUserDTO(
    val name: String,

    val email: String,

    val age: Int,

    val birthDate: LocalDate,
)


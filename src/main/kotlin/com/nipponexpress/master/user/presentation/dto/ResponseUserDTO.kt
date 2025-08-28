package com.nipponexpress.master.user.presentation.dto

import java.time.LocalDate

data class ResponseUserDTO(
    val id: Long,

    val name: String,

    val email: String,

    val age: Int,

    val birthDate: LocalDate,
)


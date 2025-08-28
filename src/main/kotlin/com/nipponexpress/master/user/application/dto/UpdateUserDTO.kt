package com.nipponexpress.master.user.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class UpdateUserDTO(
    @field:Size(min = 2, max = 50, message = "Name must be between 1 and 50")
    val name: String?,

    @field:Email(message = "Invalid format")
    val email: String?,

    @field:Min(value = 16, message = "Age must be bigger than 16")
    @field:Max(value = 65, message = "Age must be smaller than 65")
    val age: Int?,

    @field:Past(message = "Birth must be past date")
    val birthDate: LocalDate?,
)


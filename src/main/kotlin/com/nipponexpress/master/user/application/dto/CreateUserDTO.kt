package com.nipponexpress.master.user.application.dto

import jakarta.validation.constraints.*
import java.time.LocalDate

data class CreateUserDTO(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 50, message = "Name must be between 1 and 50")
    val name: String,

    @field:Email(message = "Invalid format")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:Min(value = 16, message = "Age must be bigger than 16")
    @field:Max(value = 65, message = "Age must be smaller than 65")
    val age: Int,

    @field:Past(message = "Birth must be past date")
    val birthDate: LocalDate?,
)


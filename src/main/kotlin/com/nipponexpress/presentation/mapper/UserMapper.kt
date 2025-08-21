package com.nipponexpress.presentation.mapper

import com.nipponexpress.domain.model.User
import com.nipponexpress.presentation.dto.ResponseUserDTO
import org.mapstruct.Mapper

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/08/21
 */
@Mapper(componentModel = "cdi")
interface UserMapper {
    fun toResponseDTO(user: User): ResponseUserDTO
}

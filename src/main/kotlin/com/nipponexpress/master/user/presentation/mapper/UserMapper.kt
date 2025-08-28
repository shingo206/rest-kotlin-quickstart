package com.nipponexpress.master.user.presentation.mapper

import com.nipponexpress.master.user.domain.model.User
import com.nipponexpress.master.user.presentation.dto.ResponseUserDTO
import org.mapstruct.Mapper
import org.mapstruct.NullValueMappingStrategy
import org.mapstruct.NullValuePropertyMappingStrategy

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/08/21
 */
@Mapper(
    componentModel = "cdi",
    // ✅ Global null handling strategies
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UserMapper {
    fun toResponseDTO(user: User?): ResponseUserDTO
}

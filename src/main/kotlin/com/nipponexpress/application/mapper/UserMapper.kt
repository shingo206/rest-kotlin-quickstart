package com.nipponexpress.application.mapper

import com.nipponexpress.application.dto.CreateUserDTO
import com.nipponexpress.domain.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/07/17
 */
@Mapper(componentModel = "cdi")
interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toModel(dto: CreateUserDTO): User
}

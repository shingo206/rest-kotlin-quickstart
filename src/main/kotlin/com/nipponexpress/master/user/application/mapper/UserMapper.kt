package com.nipponexpress.master.user.application.mapper

import com.nipponexpress.master.user.application.dto.CreateUserDTO
import com.nipponexpress.master.user.application.dto.UpdateUserDTO
import com.nipponexpress.master.user.domain.model.User
import org.mapstruct.*
import java.time.LocalDateTime

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/07/17
 */
@Mapper(
    componentModel = "cdi",
    // ✅ Global null handling strategies
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toModel(dto: CreateUserDTO): User

    @AfterMapping
    fun setCreatedAtAndActive(@MappingTarget user: User) {
        user.active = true
        user.createdAt = LocalDateTime.now()
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun applyToModel(dto: UpdateUserDTO, @MappingTarget user: User): User

    @AfterMapping
    fun setUpdatedAt(dto: UpdateUserDTO, @MappingTarget user: User) {
        user.updatedAt = LocalDateTime.now()
    }
}

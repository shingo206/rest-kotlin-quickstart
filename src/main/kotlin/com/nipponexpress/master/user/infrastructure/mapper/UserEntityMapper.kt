package com.nipponexpress.master.user.infrastructure.mapper

import com.nipponexpress.master.user.domain.model.User
import com.nipponexpress.master.user.infrastructure.persistence.UserEntity
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValueMappingStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import java.time.LocalDateTime

/**
 * @project rest-kotlin-quickstart
 * @author Shingo
 * @version 1.0
 * @since 2025/08/27
 */
@Mapper(
    componentModel = "cdi",
    // ✅ Global null handling strategies
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UserEntityMapper {
    fun toEntity(user: User): UserEntity
    fun toEntityList(users: List<User>): List<UserEntity>
    fun toModel(entity: UserEntity): User
    fun toModelList(entities: List<UserEntity>): List<User>

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    fun toApplyToModel(user: User, @MappingTarget entity: UserEntity): UserEntity

    @AfterMapping
    fun setUpdatedAt(@MappingTarget entity: UserEntity, user: User) {
        if (user.id != null && user.id != 0L) entity.updatedAt = LocalDateTime.now()
    }

    @AfterMapping
    fun setUserIdFromEntity(entity: UserEntity, @MappingTarget user: User) {
        user.id = entity.id ?: 0L
    }
}

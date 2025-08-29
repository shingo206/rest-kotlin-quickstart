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
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UserEntityMapper {

    /**
     * Convert User to UserEntity (includes ID for existing entities)
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(user: User): UserEntity
    fun toEntityList(users: List<User>): List<UserEntity>
    fun toModel(entity: UserEntity): User
    fun toModelList(entities: List<UserEntity>): List<User>

    /**
     * Apply User data to existing entity (preserves ID and createdAt)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    fun toApplyToModel(user: User, @MappingTarget entity: UserEntity): UserEntity

    @AfterMapping
    fun setTimestamps(@MappingTarget entity: UserEntity, user: User) {
        val now = LocalDateTime.now()

        if (user.id == null) entity.createdAt = now
        entity.updatedAt = now
    }

    /**
     * Set user ID from entity after mapping
     */
    @AfterMapping
    fun setUserIdFromEntity(entity: UserEntity, @MappingTarget user: User) {
        user.id = entity.id ?: 0L
    }
}

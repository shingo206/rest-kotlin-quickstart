package com.nipponexpress.infrastructure.mapper

import com.nipponexpress.domain.model.User
import com.nipponexpress.infrastructure.persistence.UserEntity
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import java.time.LocalDateTime

/**
 * @project rest-kotlin-quickstart
 * @author Shingo
 * @version 1.0
 * @since 2025/08/27
 */
@Mapper(componentModel = "cdi")
interface UserEntityMapper {
    fun toEntity(user: User): UserEntity
    fun toEntityList(users: List<User>): List<UserEntity>
    fun toModel(entity: UserEntity): User
    fun toModelList(entities: List<UserEntity>): List<User>
    fun toApplyToModel(user: User, @MappingTarget entity: UserEntity): User

    @AfterMapping
    fun setUpdatedAt(@MappingTarget entity: UserEntity, user: User) {
        if (user.id != null && user.id != 0L) entity.updatedAt = LocalDateTime.now()
    }
}

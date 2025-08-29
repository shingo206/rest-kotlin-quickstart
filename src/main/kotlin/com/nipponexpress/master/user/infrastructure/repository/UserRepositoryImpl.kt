package com.nipponexpress.master.user.infrastructure.repository

import com.nipponexpress.master.user.domain.model.User
import com.nipponexpress.master.user.domain.repository.UserRepository
import com.nipponexpress.master.user.infrastructure.mapper.UserEntityMapper
import com.nipponexpress.master.user.infrastructure.persistence.UserEntity
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

/**
 * @project rest-kotlin-quickstart
 * @author Shingo.Tamura
 * @version 1.0
 * @since 2025/07/10
 */
@ApplicationScoped
class UserRepositoryImpl(
    private val entityMapper: UserEntityMapper,
) : UserRepository, PanacheRepository<UserEntity> {
    override fun all(page: Page): Uni<List<User>> = findAll()
        .page<UserEntity>(page)
        .list<UserEntity>()
        .map(entityMapper::toModelList)

    override fun countByUserName(name: String): Uni<Long> =
        count("name", name)

    override fun countByEmail(email: String): Uni<Long> =
        count("email", email)

    override fun findUserById(id: Long): Uni<User?> = findById(id)
        .map(entityMapper::toModel)

    override fun findByName(name: String): Uni<User?> = find("name", name)
        .firstResult<UserEntity>()
        .map(entityMapper::toModel)

    override fun findNameContains(name: String): Uni<List<User>> =
        find("name LIKE %$name%").list<UserEntity>()
            .map(entityMapper::toModelList)


    override fun findByBirthDate(
        birthDate: LocalDate,
        page: Page
    ): Uni<List<User>> = find("birthDate", birthDate)
        .page<UserEntity>(page)
        .list<UserEntity>()
        .map(entityMapper::toModelList)

    override fun findByAgeGreaterThan(
        age: Int,
        page: Page
    ): Uni<List<User>> = find("age > ?1", age)
        .page<UserEntity>(page)
        .list<UserEntity>()
        .map(entityMapper::toModelList)

    override fun findByAgeLessThan(
        age: Int,
        page: Page
    ): Uni<List<User>> = find("age < ?1", age)
        .page<UserEntity>(page)
        .list<UserEntity>()
        .map(entityMapper::toModelList)

    override fun save(user: User): Uni<User> =
        if (user.id == null || user.id == 0L) {
            val userEntity = entityMapper.toEntity(user)
            persistAndFlush(userEntity).map(entityMapper::toModel)
        } else {
            findById(user.id!!)
                .flatMap { existingEntity ->
                    entityMapper.toApplyToModel(user, existingEntity)
                    persistAndFlush(existingEntity).map(entityMapper::toModel)
                }
        }


    override fun deleteUserById(id: Long): Uni<Boolean> = deleteById(id)
}

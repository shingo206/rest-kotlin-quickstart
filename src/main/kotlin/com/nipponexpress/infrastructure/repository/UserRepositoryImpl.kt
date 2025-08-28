package com.nipponexpress.infrastructure.repository

import com.nipponexpress.domain.model.User
import com.nipponexpress.domain.repository.UserRepository
import com.nipponexpress.infrastructure.mapper.UserEntityMapper
import com.nipponexpress.infrastructure.persistence.UserEntity
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
        .map { entityMapper.toModelList(it) }

    override fun countByUserName(name: String): Uni<Long> =
        count("name", name)

    override fun countByEmail(email: String): Uni<Long> =
        count("email", email)

    override fun findUserById(id: Long): Uni<User> = findById(id)
        .map { entityMapper.toModel(it) }

    override fun findByName(name: String): Uni<User> = find("name", name)
        .firstResult<UserEntity>()
        .map { entityMapper.toModel(it) }

    override fun findNameContains(name: String): Uni<List<User>> =
        find("name LIKE %$name%").list<UserEntity>()
            .map { entityMapper.toModelList(it) }


    override fun findByBirthDate(
        birthDate: LocalDate,
        page: Page
    ): Uni<List<User>> = find("birthDate", birthDate)
        .page<UserEntity>(page)
        .list<UserEntity>()
        .map { entityMapper.toModelList(it) }

    override fun findByAgeGreaterThan(
        age: Int,
        page: Page
    ): Uni<List<User>> = find("age > ?1", age)
        .page<UserEntity>(page)
        .list<UserEntity>()
        .map { entityMapper.toModelList(it) }

    override fun findByAgeLessThan(
        age: Int,
        page: Page
    ): Uni<List<User>> = find("age < ?1", age)
        .page<UserEntity>(page)
        .list<UserEntity>()
        .map { entityMapper.toModelList(it) }

    override fun save(user: User): Uni<User> =
        persist(entityMapper.toEntity(user))
            .map { entityMapper.toModel(it) }


    override fun deleteUserById(id: Long): Uni<Boolean> = deleteById(id)
}

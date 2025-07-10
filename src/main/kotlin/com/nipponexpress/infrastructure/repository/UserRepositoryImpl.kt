package com.nipponexpress.infrastructure.repository

import com.nipponexpress.domain.model.User
import com.nipponexpress.domain.repository.UserRepository
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/07/10
 */
@ApplicationScoped
class UserRepositoryImpl : UserRepository, PanacheRepository<User> {
    override fun all(page: Page): Uni<List<User>> = findAll().page<User>(page).list()

    override fun findUserById(id: Long): Uni<User> = findById(id)

    override fun findByName(name: String): Uni<User> = find("name", name).firstResult()

    override fun findByBirthDate(
        birthDate: LocalDate,
        page: Page
    ): Uni<List<User>> = find("birthDate", birthDate).page<User>(page).list()

    override fun findByAgeGreaterThan(
        age: Int,
        page: Page
    ): Uni<List<User>> = find("age > ?1", age).page<User>(page).list()

    override fun findByAgeLessThan(
        age: Int,
        page: Page
    ): Uni<List<User>> = find("age < ?1", age).page<User>(page).list()

    override fun save(user: User): Uni<User> = persist(user).replaceWith { user }

    override fun deleteById(id: Long): Uni<Boolean> = deleteById(id)
}

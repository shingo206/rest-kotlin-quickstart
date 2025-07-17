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
 * @author Shingo.Tamura
 * @version 1.0
 * @since 2025/07/10
 */
@ApplicationScoped
class UserRepositoryImpl : UserRepository, PanacheRepository<User> {
    override fun all(page: Page): Uni<List<User>> = findAll().page<User>(page).list()
    override fun countByUserName(name: String): Uni<Long> =
        count("name", name)

    override fun countByEmail(email: String): Uni<Long> =
        count("email", email)

    override fun findUserById(id: Long): Uni<User> = findById(id)

    override fun findByName(name: String): Uni<User> = find("name", name).firstResult()

    override fun findNameContains(name: String): Uni<List<User>> =
        find("name LIKE %$name%").list()


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

    override fun deleteUserById(id: Long): Uni<Boolean> = deleteById(id)
}

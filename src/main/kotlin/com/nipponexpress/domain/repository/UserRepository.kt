package com.nipponexpress.domain.repository

import com.nipponexpress.domain.model.User
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import java.time.LocalDate

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/07/10
 */
interface UserRepository {
    fun all(page: Page): Uni<List<User>>
    fun countByUserName(name: String): Uni<Long>
    fun countByEmail(email: String): Uni<Long>
    fun findUserById(id: Long): Uni<User>
    fun findByName(name: String): Uni<User>
    fun findNameContains(name: String): Uni<List<User>>
    fun findByBirthDate(birthDate: LocalDate, page: Page): Uni<List<User>>
    fun findByAgeGreaterThan(age: Int, page: Page): Uni<List<User>>
    fun findByAgeLessThan(age: Int, page: Page): Uni<List<User>>
    fun save(user: User): Uni<User>
    fun deleteUserById(id: Long): Uni<Boolean>
}

package com.nipponexpress.master.user.application.service

import com.nipponexpress.master.user.application.dto.CreateUserDTO
import com.nipponexpress.master.user.application.dto.UpdateUserDTO
import com.nipponexpress.master.user.application.exception.DuplicateEmailException
import com.nipponexpress.master.user.application.exception.DuplicateUserNameException
import com.nipponexpress.master.user.application.mapper.UserMapper
import com.nipponexpress.master.user.domain.exception.UserDoesNotExistsException
import com.nipponexpress.master.user.domain.model.User
import com.nipponexpress.master.user.domain.repository.UserRepository
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/07/17
 */
@ApplicationScoped
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {
    @WithTransaction
    fun createUser(dto: CreateUserDTO): Uni<User> =
        countUserByName(dto.name)
            .flatMap { countUserByEmail(dto.email) }
            .flatMap { userRepository.save(userMapper.toModel(dto)) }

    fun countUserByName(name: String): Uni<Long> = userRepository.countByUserName(name).flatMap {
        if (it > 0) Uni.createFrom()
            .failure(DuplicateUserNameException("User name: $name already exists"))
        else Uni.createFrom().item(it)
    }

    fun countUserByEmail(email: String): Uni<Long> = userRepository.countByEmail(email).flatMap {
        if (it > 0) Uni.createFrom()
            .failure(DuplicateEmailException("The email: $it already exists"))
        else Uni.createFrom().item(it)
    }

    fun findUserById(id: Long): Uni<User?> = userRepository.findUserById(id)

    @WithTransaction
    fun updateUser(id: Long, dto: UpdateUserDTO): Uni<User> =
        findUserById(id)
            .onItem().ifNull().failWith(UserDoesNotExistsException("user does not exists with id: $id"))
            .flatMap { existingUser ->
                val checkUsernameUnique = if (dto.name != null && dto.name != existingUser?.name)
                    countUserByName(dto.name)
                else Uni.createFrom().item(0L)

                val checkEmailUnique = if (dto.email != null && dto.email != existingUser?.email)
                    countUserByEmail(dto.email)
                else Uni.createFrom().item(0L)

                checkUsernameUnique
                    .flatMap { checkEmailUnique }
                    .flatMap { userRepository.save(userMapper.applyToModel(dto, existingUser!!)) }
            }

    @WithTransaction
    fun deleteUserById(id: Long): Uni<Boolean> =
        findUserById(id)
            .onItem().ifNull().failWith(UserDoesNotExistsException("user does not exists with id: $id"))
            .flatMap { userRepository.deleteUserById(id) }
}

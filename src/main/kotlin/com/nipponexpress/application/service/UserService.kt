package com.nipponexpress.application.service

import com.nipponexpress.application.dto.CreateUserDTO
import com.nipponexpress.application.dto.UpdateUserDTO
import com.nipponexpress.application.exception.DuplicateEmailException
import com.nipponexpress.application.exception.DuplicateUserNameException
import com.nipponexpress.application.mapper.UserMapper
import com.nipponexpress.domain.exception.UserDoesNotExistsException
import com.nipponexpress.domain.model.User
import com.nipponexpress.domain.repository.UserRepository
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

    fun findUserById(id: Long): Uni<User> = userRepository.findUserById(id)
        .onItem().ifNull().failWith(UserDoesNotExistsException("user does not exists with id: $id"))


    fun updateUser(id: Long, dto: UpdateUserDTO): Uni<User> =
        findUserById(id)
            .flatMap { existingUser ->
                val checkUsernameUnique = if (dto.name != null && dto.name != existingUser.name)
                    countUserByName(dto.name)
                else Uni.createFrom().item(0L)

                val checkEmailUnique = if (dto.email != null && dto.email != existingUser.email)
                    countUserByEmail(dto.email)
                else Uni.createFrom().item(0L)

                checkUsernameUnique
                    .flatMap { checkEmailUnique }
                    .flatMap { userRepository.save(userMapper.applyToModel(dto, existingUser)) }
            }

    fun deleteUserById(id: Long): Uni<Boolean> =
        findUserById(id).flatMap {
            userRepository.deleteUserById(id)
        }
}

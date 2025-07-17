package com.nipponexpress.application.service

import com.nipponexpress.application.dto.CreateUserDTO
import com.nipponexpress.application.exception.DuplicateEmailException
import com.nipponexpress.application.exception.DuplicateUserNameException
import com.nipponexpress.application.mapper.UserMapper
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
    fun createUser(user: CreateUserDTO): Uni<User> =
        userRepository.countByUserName(user.name)
            .flatMap {
                if (it > 0) Uni.createFrom()
                    .failure(DuplicateUserNameException("User name: ${user.name} already exists"))
                else userRepository.countByEmail(user.email)
            }
            .flatMap {
                if (it > 0) Uni.createFrom()
                    .failure(DuplicateEmailException("The email: $it already exists"))
                else userRepository.save(userMapper.toModel(user))
            }
}

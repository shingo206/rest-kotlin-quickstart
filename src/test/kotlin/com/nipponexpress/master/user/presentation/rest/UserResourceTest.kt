package com.nipponexpress.master.user.presentation.rest

import com.nipponexpress.master.user.application.dto.CreateUserDTO
import com.nipponexpress.master.user.application.exception.DuplicateUserNameException
import com.nipponexpress.master.user.application.service.UserService
import com.nipponexpress.master.user.domain.model.User
import com.nipponexpress.master.user.presentation.dto.ApiResponse
import com.nipponexpress.master.user.presentation.dto.ResponseUserDTO
import com.nipponexpress.master.user.presentation.exception.ReactiveExceptionMapper
import com.nipponexpress.master.user.presentation.mapper.UserMapper
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.hamcrest.CoreMatchers
import org.jboss.resteasy.reactive.RestResponse
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/08/28
 */
@QuarkusTest
class UserResourceTest {
    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var userMapper: UserMapper

    @Inject
    lateinit var exceptionMapper: ReactiveExceptionMapper

    @Test
    fun `given valid CreateUserDTO when createUser is called then return 201 with created user`() {
        val id = 1L
        val name = "John Doe"
        val email = "john@example.com"
        val age = 20
        val birthday = LocalDate.of(1980, 1, 1)
        val createDto = CreateUserDTO(name, email, age, birthday)
        val user = User(id, name, email, age, birthday)
        val responseDto = ResponseUserDTO(id, name, email, age, birthday)

        Mockito.`when`(userService.createUser(createDto)).thenReturn(Uni.createFrom().item(user))
        Mockito.`when`(userMapper.toResponseDTO(user)).thenReturn(responseDto)

        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(createDto)
            .`when`()
            .post("/users")
            .then()
            .statusCode(201)
            .body("success", CoreMatchers.equalTo(true))
            .body("data.name", CoreMatchers.equalTo(name))
            .body("data.email", CoreMatchers.equalTo(email))
            .body("data.age", CoreMatchers.equalTo(age))
            .body("data.birthday", CoreMatchers.equalTo(birthday.format(DateTimeFormatter.ISO_DATE)))
    }

    @Test
    fun `given duplicate username when createUser is called then return 409 conflict`() {
        val name = "John Doe"
        val email = "john@example.com"
        val age = 20
        val birthday = LocalDate.of(1980, 1, 1)
        val createDto = CreateUserDTO(name, email, age, birthday)
        val message = "Username already exists"

        // Simulate service throwing duplicate username exception
        val duplicateException = DuplicateUserNameException(message)

        Mockito.`when`(userService.createUser(createDto))
            .thenReturn(Uni.createFrom().failure(duplicateException))

        // Mock exception mapper to return 409 Conflict
        val apiResponse = ApiResponse<Any>(
            statusCode = 409,
            success = false,
            message = message,
            data = null
        )
        val restResponse = RestResponse.status(Response.Status.CONFLICT, apiResponse)

        Mockito.`when`(exceptionMapper.handleException<Any>(duplicateException))
            .thenReturn(Uni.createFrom().item(restResponse))

        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(createDto)
            .`when`()
            .post("/users")
            .then()
            .statusCode(409)
            .body("success", CoreMatchers.equalTo(false))
            .body("message", CoreMatchers.equalTo(message))
    }

}

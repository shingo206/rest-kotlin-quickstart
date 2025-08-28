package com.nipponexpress.presentation.rest

import com.nipponexpress.application.dto.CreateUserDTO
import com.nipponexpress.application.dto.UpdateUserDTO
import com.nipponexpress.application.service.UserService
import com.nipponexpress.presentation.dto.ApiResponse
import com.nipponexpress.presentation.dto.ResponseUserDTO
import com.nipponexpress.presentation.exception.ReactiveExceptionMapper
import com.nipponexpress.presentation.mapper.UserMapper
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/08/07
 */

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
class UserResource(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val exceptionMapper: ReactiveExceptionMapper
) {
    // Create
    @POST
    fun createUser(dto: CreateUserDTO): Uni<RestResponse<ApiResponse<ResponseUserDTO>>> =
        userService.createUser(dto)
            .map { user ->
                val responseUser = userMapper.toResponseDTO(user)
                val apiResponse = ApiResponse(
                    statusCode = 201,
                    success = true,
                    message = "User has created",
                    data = responseUser
                )
                RestResponse.ResponseBuilder
                    .created<ApiResponse<ResponseUserDTO>>(URI.create("/users/{user.id}"))
                    .entity(apiResponse)
                    .build()
            }
            .onFailure().recoverWithUni { throwable ->
                exceptionMapper.handleException(throwable)
            }


    // Read
    @GET
    @Path("/{id}")
    fun getUserById(@PathParam("id") id: Long): Uni<RestResponse<ApiResponse<ResponseUserDTO>>> =
        userService.findUserById(id)
            .map { user ->
                val responseUser = userMapper.toResponseDTO(user)
                val apiResponse = ApiResponse(
                    statusCode = 200,
                    success = true,
                    message = "User has been retrieved",
                    data = responseUser
                )
                RestResponse.ResponseBuilder
                    .ok<ApiResponse<ResponseUserDTO>>()
                    .entity(apiResponse)
                    .build()
            }
            .onFailure().recoverWithUni { throwable ->
                exceptionMapper.handleException(throwable)
            }


    // Update
    @PUT
    @Path("/{id}")
    fun updateUser(id: Long, dto: UpdateUserDTO): Uni<RestResponse<ApiResponse<ResponseUserDTO>>> =
        userService.updateUser(id, dto)
            .map { user ->
                val responseUser = userMapper.toResponseDTO(user)
                val apiResponse = ApiResponse(
                    statusCode = 200,
                    success = true,
                    message = "User has been updated",
                    data = responseUser
                )
                RestResponse.ResponseBuilder
                    .ok<ApiResponse<ResponseUserDTO>>()
                    .entity(apiResponse)
                    .build()
            }
            .onFailure().recoverWithUni { throwable ->
                exceptionMapper.handleException(throwable)
            }


    // Delete
    @DELETE
    @Path("/{id}")
    fun deleteUserById(@PathParam("id") id: Long): Uni<RestResponse<ApiResponse<Boolean>>> =
        userService.deleteUserById(id)
            .map {
                val apiResponse = ApiResponse(
                    statusCode = 204,
                    success = true,
                    message = "User has been deleted",
                    data = true
                )
                RestResponse.ResponseBuilder
                    .noContent<ApiResponse<Boolean>>()
                    .entity(apiResponse)
                    .build()
            }
            .onFailure().recoverWithUni { throwable ->
                exceptionMapper.handleException(throwable)
            }


}

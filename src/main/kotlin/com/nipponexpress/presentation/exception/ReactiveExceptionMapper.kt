package com.nipponexpress.presentation.exception

import com.nipponexpress.application.exception.DuplicateEmailException
import com.nipponexpress.application.exception.DuplicateUserNameException
import com.nipponexpress.domain.exception.UserDoesNotExistsException
import com.nipponexpress.presentation.dto.ApiResponse
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.RestResponse.Status.*
import org.slf4j.LoggerFactory


/**
 * @project rest-kotlin-quickstart
 * @author Shingo. Tamura
 * @version 1.0
 * @since 2025/08/26
 */
@ApplicationScoped
class ReactiveExceptionMapper {
    private val logger = LoggerFactory.getLogger(ReactiveExceptionMapper::class.java)

    fun <T> handleException(exception: Throwable): Uni<RestResponse<ApiResponse<T>>> {
        logger.error("Exception caught by GlobalExceptionMapper $exception")

        val (status, message) = when (exception) {
            is UserDoesNotExistsException -> Pair(NOT_FOUND, exception.message ?: "User not found")
            is DuplicateUserNameException -> Pair(CONFLICT, exception.message ?: "Username already exists")
            is DuplicateEmailException -> Pair(CONFLICT, exception.message ?: "E-mail address already exists")
            is IllegalArgumentException -> Pair(BAD_REQUEST, exception.message ?: "Invalid request")
            else -> {
                logger.error("Unhandled exception: ${exception::class.simpleName}", exception)
                Pair(INTERNAL_SERVER_ERROR, exception.message ?: "Internal server error")
            }
        }

        val apiResponse = ApiResponse<T>(
            statusCode = status.statusCode,
            success = false,
            message = message,
            data = null
        )


        val restResponse = RestResponse.status(status, apiResponse)

        return Uni.createFrom().item(restResponse)
    }

    fun <T> Uni<RestResponse<ApiResponse<T>>>.onFailureHandle(): Uni<RestResponse<ApiResponse<T>>> =
        this.onFailure().recoverWithUni { throwable ->
            handleException(throwable)
        }
}

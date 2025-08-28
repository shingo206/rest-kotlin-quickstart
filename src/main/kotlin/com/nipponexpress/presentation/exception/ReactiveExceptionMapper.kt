package com.nipponexpress.presentation.exception

import com.nipponexpress.application.exception.DuplicateEmailException
import com.nipponexpress.application.exception.DuplicateUserNameException
import com.nipponexpress.domain.exception.UserDoesNotExistsException
import com.nipponexpress.presentation.dto.ApiResponse
import io.smallrye.mutiny.Uni
import io.vertx.core.impl.logging.LoggerFactory
import jakarta.enterprise.context.ApplicationScoped
import org.jboss.resteasy.reactive.RestResponse

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/08/27
 */
@ApplicationScoped
class ReactiveExceptionMapper {
    private val logger = LoggerFactory.getLogger(ReactiveExceptionMapper::class.java)

    fun <T> handleException(throwable: Throwable): Uni<RestResponse<ApiResponse<T>>> {
        logger.error("Exception caught by ReactiveExceptionMapper", throwable)

        val (status, message) = when (throwable) {
            is DuplicateUserNameException -> Pair(
                RestResponse.Status.CONFLICT,
                throwable.message ?: "User name already exists"
            )

            is DuplicateEmailException -> Pair(
                RestResponse.Status.CONFLICT,
                throwable.message ?: "E-mail already exists"
            )

            is UserDoesNotExistsException -> Pair(
                RestResponse.Status.NOT_FOUND,
                throwable.message ?: "User not found"
            )

            is IllegalArgumentException -> Pair(
                RestResponse.Status.BAD_REQUEST,
                throwable.message ?: "Invalid request"
            )

            else -> Pair(RestResponse.Status.INTERNAL_SERVER_ERROR, throwable.message ?: "Internal Server Error")
        }
        val apiResponse = ApiResponse<T>(
            statusCode = status.statusCode,
            success = false,
            message = message,
            data = null,
        )
        val restResponse = RestResponse.status(status, apiResponse)

        return Uni.createFrom().item(restResponse)
    }
}

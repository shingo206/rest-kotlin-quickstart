package com.nipponexpress.master.user.presentation.exception

import com.nipponexpress.master.user.application.exception.DuplicateEmailException
import com.nipponexpress.master.user.application.exception.DuplicateUserNameException
import com.nipponexpress.master.user.domain.exception.UserDoesNotExistsException
import com.nipponexpress.master.user.presentation.dto.ApiResponse
import io.smallrye.mutiny.Uni
import io.vertx.core.impl.logging.LoggerFactory
import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.ConstraintViolationException
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

            is ConstraintViolationException -> {
                val violations = throwable.constraintViolations
                val errorMessage = violations.joinToString(", ") { it.message }
                Pair(
                    RestResponse.Status.BAD_REQUEST,
                    "Validation failed: $errorMessage"
                )
            }

            is IllegalArgumentException -> Pair(
                RestResponse.Status.BAD_REQUEST,
                throwable.message ?: "Invalid request"
            )

            else -> {
                val exceptionMessage = throwable.message ?: ""

                if (exceptionMessage.contains("ConstraintViolationException") ||
                    exceptionMessage.contains("interpolatedMessage")) {

                    // Extract validation message using regex
                    val validationMessage = extractValidationMessage(exceptionMessage)
                    Pair(RestResponse.Status.BAD_REQUEST, validationMessage)
                } else {
                    Pair(RestResponse.Status.INTERNAL_SERVER_ERROR, "Internal Server Error")
                }
            }
        }
        val apiResponse = ApiResponse<T>(
            statusCode = status.statusCode,
            success = false,
            message = message
        )
        val restResponse = RestResponse.status(status, apiResponse)

        return Uni.createFrom().item(restResponse)
    }

    private fun extractValidationMessage(fullMessage: String): String {
        val regex = Regex("interpolatedMessage='([^']+)'")
        val matchResult = regex.find(fullMessage)

        return if (matchResult != null) {
            matchResult.groupValues[1]
        } else {
            val lines = fullMessage.split("\n")
            val constraintLine = lines.find { it.contains("ConstraintViolationImpl") }
            constraintLine?.let {
                val messageRegex = Regex("message='([^']+)'")
                messageRegex.find(it)?.groupValues?.get(1)
            } ?: "Validation error"
        }
    }
}

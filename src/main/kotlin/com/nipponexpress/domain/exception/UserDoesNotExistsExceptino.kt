package com.nipponexpress.domain.exception

import jakarta.ws.rs.NotFoundException

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/07/17
 */
class UserDoesNotExistsExceptino(message: String) : NotFoundException(message) {
}

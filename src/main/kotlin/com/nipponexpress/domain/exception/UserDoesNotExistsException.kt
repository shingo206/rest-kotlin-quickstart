package com.nipponexpress.domain.exception

/**
 * @project rest-kotlin-quickstart
 * @author Shingo
 * @version 1.0
 * @since 2025/07/17
 */
class UserDoesNotExistsException(message: String) : DomainException(message)

abstract class DomainException(message: String) : RuntimeException(message)

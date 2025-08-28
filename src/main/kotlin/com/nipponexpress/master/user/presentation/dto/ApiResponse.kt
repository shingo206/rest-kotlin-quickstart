package com.nipponexpress.master.user.presentation.dto

/**
 * @project rest-kotlin-quickstart
 * @author 9000120000
 * @version 1.0
 * @since 2025/08/21
 */
data class ApiResponse<T>(
    val statusCode: Int,
    val success: Boolean,
    val message: String,
    val data: T?
)

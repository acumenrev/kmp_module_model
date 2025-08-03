
package org.example.project.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    @SerialName("created_at")
    val createdAt: String,
    val isActive: Boolean = true
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    @SerialName("error_code")
    val errorCode: Int? = null
)

@Serializable
data class UserSettings(
    val userId: String,
    val theme: String = "light",
    val notifications: Boolean = true,
    val language: String = "en"
)

// Enum example
@Serializable
enum class UserStatus {
    @SerialName("active")
    ACTIVE,
    @SerialName("inactive")
    INACTIVE,
    @SerialName("pending")
    PENDING,
    @SerialName("suspended")
    SUSPENDED
}

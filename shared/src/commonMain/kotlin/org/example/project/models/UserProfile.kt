package org.example.project.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserProfile(
    val user: User,
    val avatar: String? = null,
    val bio: String? = null,
    val followers: Int = 0,
    val following: Int = 0
)

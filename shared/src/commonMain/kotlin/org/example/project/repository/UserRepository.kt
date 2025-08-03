package org.example.project.repository

import kotlinx.coroutines.flow.asStateFlow
import org.example.project.models.User
import org.example.project.models.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class UserRepository {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: Flow<List<User>> = _users.asStateFlow()

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: Flow<UserProfile?> = _currentUser.asStateFlow()

    suspend fun fetchUsers(): Result<List<User>> {
        return try {
            // Simulate API call
            val mockUsers = listOf(
                User(
                    id = "1",
                    name = "John Doe",
                    email = "john@example.com",
                    createdAt = "2024-01-01T00:00:00Z"
                ),
                User(
                    id = "2",
                    name = "Jane Smith",
                    email = "jane@example.com",
                    createdAt = "2024-01-02T00:00:00Z"
                )
            )
            _users.value = mockUsers
            Result.success(mockUsers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: String): Result<UserProfile?> {
        return try {
            val user = _users.value.find { it.id == userId }
            val profile = user?.let {
                UserProfile(
                    user = it,
                    avatar = "https://example.com/avatar/${it.id}.jpg",
                    bio = "This is ${it.name}'s bio",
                    followers = 100,
                    following = 50
                )
            }
            _currentUser.value = profile
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun clearCurrentUser() {
        _currentUser.value = null
    }
}
package org.example.project.usecases

import kotlinx.coroutines.flow.Flow
import org.example.project.models.User
import org.example.project.models.UserProfile
import org.example.project.repository.UserRepository

class UserUseCase(private val repository: UserRepository) {

    val users: Flow<List<User>> = repository.users
    val currentUser: Flow<UserProfile?> = repository.currentUser

    suspend fun loadUsers(): Result<List<User>> {
        return repository.fetchUsers()
    }

    suspend fun selectUser(userId: String): Result<UserProfile?> {
        return repository.getUserProfile(userId)
    }

    fun logout() {
        repository.clearCurrentUser()
    }

    fun validateEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    fun isUserActive(user: User): Boolean {
        return user.isActive
    }
}
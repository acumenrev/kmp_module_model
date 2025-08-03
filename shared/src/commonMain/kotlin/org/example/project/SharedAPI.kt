package org.example.project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.models.User
import org.example.project.models.UserProfile
import org.example.project.repository.UserRepository
import org.example.project.usecases.UserUseCase

class SharedAPI {
    private val repository = UserRepository()
    private val userUseCase = UserUseCase(repository)
    private val scope = CoroutineScope(Dispatchers.Main)

    // iOS-friendly callback-based functions
    fun loadUsers(
        onSuccess: (List<User>) -> Unit,
        onError: (String) -> Unit
    ) {
        scope.launch {
            userUseCase.loadUsers()
                .onSuccess { users -> onSuccess(users) }
                .onFailure { error -> onError(error.message ?: "Unknown error") }
        }
    }

    fun selectUser(
        userId: String,
        onSuccess: (UserProfile?) -> Unit,
        onError: (String) -> Unit
    ) {
        scope.launch {
            val reuslt: Result<UserProfile?> = userUseCase.selectUser(userId)
            reuslt.onSuccess { profile -> onSuccess(profile) }
                .onFailure { error -> onError(error.message ?: "Unknown error" ) }

        }
    }

    fun validateEmail(email: String): Boolean {
        return userUseCase.validateEmail(email)
    }

    fun logout() {
        userUseCase.logout()
    }

    // For advanced iOS developers who want to use Combine
    fun observeUsers(): kotlinx.coroutines.flow.Flow<List<User>> {
        return userUseCase.users
    }

    fun observeCurrentUser(): kotlinx.coroutines.flow.Flow<UserProfile?> {
        return userUseCase.currentUser
    }
}
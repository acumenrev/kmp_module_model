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

    // MARK: - Async/Await API (Modern approach)

    @Throws(Exception::class)
    suspend fun loadUsers(): List<User> {
        val result = userUseCase.loadUsers()
        return result.getOrThrow()
    }

    @Throws(Exception::class)
    suspend fun selectUser(userId: String): UserProfile? {
        val result = userUseCase.selectUser(userId)
        return result.getOrThrow()
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
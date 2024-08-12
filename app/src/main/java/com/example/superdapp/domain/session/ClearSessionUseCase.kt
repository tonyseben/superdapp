package com.example.superdapp.domain.session

import com.example.superdapp.data.repository.UserRepository
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.clearSession()
    }
}
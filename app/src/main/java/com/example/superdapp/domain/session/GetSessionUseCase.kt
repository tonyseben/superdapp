package com.example.superdapp.domain.session

import com.example.superdapp.data.repository.UserRepository
import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Sign.Model.Session? {
        return userRepository.getSession()?.let { topic ->
            SignClient.getActiveSessionByTopic(topic)
        }
    }
}
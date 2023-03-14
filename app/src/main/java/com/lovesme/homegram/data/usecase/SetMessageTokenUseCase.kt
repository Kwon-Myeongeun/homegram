package com.lovesme.homegram.data.usecase

import com.lovesme.homegram.data.repository.MessageTokenRepository
import javax.inject.Inject

class SetMessageTokenUseCase @Inject constructor(private val messageTokenRepository: MessageTokenRepository) {
    suspend operator fun invoke(string: String) =
        messageTokenRepository.setMessageToken(string)
}
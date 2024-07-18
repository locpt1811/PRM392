package com.example.finalproject.domain.repository

import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.model.shopping.UserProfileInfoDTO
import java.util.UUID

interface ProfileRepository {
    suspend fun getProfileUserById(userUuid: UUID): Response<UserProfileDTO>
    suspend fun getProfileUsers(): Response<List<UserProfileDTO>>
}

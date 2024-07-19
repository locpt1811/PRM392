package com.example.finalproject.domain.repository

import com.example.finalproject.common.Response
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.model.shopping.UserProfileInfoDTO
import io.github.jan.supabase.postgrest.result.PostgrestResult
import java.util.UUID

interface ProfileRepository {
    suspend fun getProfileUserById(userUuid: String): Response<UserProfileDTO>
    suspend fun getProfileUsers(): Response<List<UserProfileDTO>>
    suspend fun UpdateUserName(firstName: String, lastName: String, uid: UUID): Response<String>
    suspend fun InsertProfile(firstName: String, lastName: String, uid: String): Response<String>
}

package com.example.finalproject.data.repository

import android.util.Log
import com.example.finalproject.R
import com.example.finalproject.common.Response
import com.example.finalproject.data.mapper.toBookDTO
import com.example.finalproject.data.mapper.toDto
import com.example.finalproject.domain.repository.ProfileRepository
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.model.shopping.UserProfileInfo
import com.example.finalproject.model.shopping.UserProfileInfoDTO
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
class ProfileRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : ProfileRepository {
    override suspend fun getProfileUsers(): Response<List<UserProfileDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val profile = postgrest.from("profiles")
                    .select()
                    .decodeList<UserProfileDTO>()
                Response.Success(profile)
            } catch (e: Exception) {
                Log.e("ProfileRepo", "Get exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override suspend fun getProfileUserById(userUuid: UUID): Response<UserProfileDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val profile = postgrest.from("profiles")
                    .select {
                        filter {
                            eq("id", userUuid)
                        }
                    }
                    .decodeSingle<UserProfileDTO>()
                Response.Success(profile)
            } catch (e: Exception) {
                Log.e("ProfileRepo", "Get 1 exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }
}

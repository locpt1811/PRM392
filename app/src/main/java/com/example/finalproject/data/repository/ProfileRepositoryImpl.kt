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
import io.github.jan.supabase.postgrest.RpcMethod
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.postgrest.rpc
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

    override suspend fun getProfileUserById(userUuid: String): Response<UserProfileDTO> {
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

    override suspend fun UpdateUserName(firstName: String, lastName: String, uid: UUID): Response<String> {
        return withContext(Dispatchers.IO) {
            try {


                val profile = postgrest.from("profiles")
                    .select {
                        filter {
                            eq("id", uid)
                        }
                    }
                    .decodeSingleOrNull<UserProfileDTO>()

                if(profile != null){
                    val result = postgrest.from("profiles").update({
                        set("first_name", firstName)
                        set("last_name", lastName)
                    }) {
                        select()
                        filter {
                            eq("id", uid)
                        }
                    }.decodeSingle<UserProfileDTO>()
                }else{
                    Log.d("CreateProfile", "Get 1 exception: ${firstName} ${lastName} ${uid}")
                    val result = postgrest.from("profiles")
                        .insert(UserProfileDTO(uid.toString()
                            , firstName
                            , lastName
                            )
                        )
//                        {
//                        select()
//                        filter {
//                            eq("id", uid)
//                        }
//                    }.decodeSingle<UserProfileDTO>()
                    Log.d("CreateProfile", "Get 1 exception: ${result.toString()}")
                }




                Response.Success("true")
            } catch (e: Exception) {
                Log.e("ProfileRepo", "Get 1 exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }

    override suspend fun InsertProfile(firstName: String, lastName: String, uid: String): Response<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ProfileRepo", "Get 1 exception: ${firstName} ${lastName} ${uid}")


                val result = postgrest.from("profiles").insert(UserProfileInfoDTO(uid, firstName, lastName,"https://th.bing.com/th/id/OIP.oRoosgD6pPrJW2PXAJ-hBwHaJ4?rs=1&pid=ImgDetMain")) {
                    select()
                    filter {
                        eq("id", uid)
                    }
                }.decodeSingle<UserProfileDTO>()


                Log.d("ProfileRepo", "Get 1 exception: ${result}")
                Response.Success("true")
            } catch (e: Exception) {
                Log.e("ProfileRepo", "Get 1 exception: ${e.message}")
                Response.Error(errorMessageId = R.string.error_message_books)
            }
        }
    }


}
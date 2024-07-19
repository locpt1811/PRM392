package com.example.finalproject.presentation.home.profile

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.common.Response
import com.example.finalproject.R
import com.example.finalproject.common.helper.UiText
import com.example.finalproject.domain.repository.AuthRepository
import com.example.finalproject.domain.repository.BookRepository
import com.example.finalproject.domain.repository.ProfileRepository
import com.example.finalproject.model.shopping.UserProfileDTO
import com.example.finalproject.model.shopping.UserProfileInfoDTO
import com.example.finalproject.model.user_detail.UserDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@Stable
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val repository: BookRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    val uid: String = ""

    init {
        getUserProfileImage()
        getUserDetails()

        viewModelScope.launch {
            getUserData()
        }

        val user = auth
//        val user = auth.currentUser

        if (user == null) {
            _uiState.update {
                it.copy(
                    errorMessages = listOf(
                        UiText.StringResource(resId = R.string.unknown_error)
                    ),
                    firstNameError = false,
                    lastNameError = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
//                    name = user.displayName, email = user.email, phoneNumber = user.phoneNumber
                )
            }
        }
    }

    var updateValue by mutableStateOf("")
        private set

    var verificationCode by mutableStateOf("")
        private set

    var infoType by mutableStateOf(InfoType.NAME)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    private var storedVerificationId: String? = null

    fun updateAccountInfoValue(value: String) {
        updateValue = value
    }

    fun updateVerificationCodeValue(value: String) {
        verificationCode = value
    }

    fun updateEmailValue(value: String) {
        email = value
    }

    fun updatePasswordValue(value: String) {
        password = value
    }

    fun clearAccountInfoValue() {
        updateValue = ""
    }

    suspend fun logOut() {
        try {
            authRepository.logout()
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    suspend fun getUserData() {
        try {
            val user = authRepository.retreiveCurrentUser()
            _uiState.update {

                it.copy(
                    email = user?.email,
                    emailVerified = user?.emailVerified ?: false,
                    uid = user?.uid
                )
            }
            Log.d("ProfileViewModel", "Retrieved user: $user")
            val profileResponse = user?.let { profileRepository.getProfileUserById(it.uid) }
            if (profileResponse is Response.Success<*>) {
                val profiles = profileResponse.data as UserProfileDTO


                _uiState.update {

                    it.copy(
                        name = "${profiles.first_name} ${profiles.last_name}"
                    )
                }
            }

        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    fun updateUserPassword() {
        try {
            viewModelScope.launch {
                authRepository.updateUser {
                    password = "123"
                }
            }

        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    fun updateUsername(newFirstname: String, newLastname: String) {

        if (newFirstname.isEmpty()) {
            _uiState.update {
                it.copy(firstNameError = true,
                    errorMessages = listOf(
                        UiText.StringResource(resId = R.string.first_name_empty)
                    )
                    )



            }
            return
        }

        if (newLastname.isEmpty()) {
            _uiState.update {
                it.copy(lastNameError = true,
                     errorMessages = listOf(
                        UiText.StringResource(resId = R.string.last_name_empty) ))
            }
            return
        }

        try {
            val uid =  UUID.fromString(_uiState.value.uid)
            viewModelScope.launch {
                profileRepository.UpdateUserName(newFirstname, newLastname, uid)
            }
            _uiState.update {

                it.copy(
                    lastNameError = false,
                    name = "${newFirstname} ${newLastname}",
                    firstNameError = false

                )
            }

        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }


    fun setAccountInfoType(infoType: InfoType) {
        this.infoType = infoType

        updateValue = when (infoType) {
            InfoType.NAME -> {
                _uiState.value.name ?: ""
            }

            InfoType.MOBILE -> {
                _uiState.value.phoneNumber ?: ""
            }

            InfoType.ADDRESS -> {
                _uiState.value.userDetail?.address ?: ""
            }

            else -> {
                ""
            }
        }
    }


    private fun getUserProfileImage() {
        viewModelScope.launch(ioDispatcher) {
//            repository.getUserProfileImage().addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    _uiState.update {
//                        it.copy(photoUrl = task.result)
//                    }
//                }
//                Log.e("GET USER PROFILE IMAGE", task.exception?.stackTraceToString() ?: "error")
//            }
        }
    }


    private fun getUserDetails() {
//        viewModelScope.launch(ioDispatcher) {
//            repository.getAllUserDetails().addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    _uiState.update {
//                        it.copy(userDetail = task.result.toObject<UserDetail>())
//                    }
//                } else {
//                    Log.e("GET USER DETAILS", task.exception?.stackTraceToString() ?: "error")
//                    _uiState.update {
//                        it.copy(errorMessages = listOf(task.exception?.message?.let { message ->
//                            UiText.DynamicString(message)
//                        } ?: kotlin.run {
//                            UiText.StringResource(resId = R.string.unknown_error)
//                        }))
//                    }
//                }
//            }
//        }
    }


    data class ProfileUiState(
        val isLoading: Boolean = false,
        val errorMessages: List<UiText> = listOf(),
        val userMessages: List<UiText> = listOf(),
        val name: String? = null,
        val email: String? = null,
        val photoUrl: Uri? = null,
        val phoneNumber: String? = null,
        val verifyPhoneNumberState: VerifyPhoneNumberState = VerifyPhoneNumberState(),
        val userDetail: UserDetail? = null,
        val deleteAccountState: DeleteAccountState = DeleteAccountState(),
        val updateAccountInfoDialogState: DialogUiState = DialogUiState.DialogInactive,
        val imageCropperDialogUiState: DialogUiState = DialogUiState.DialogInactive,
        val emailVerified: Boolean = true,
        val uid: String? = null,
        val firstNameError: Boolean? = false,
        val lastNameError: Boolean? = false,
    )

    data class VerifyPhoneNumberState(
        val verifyPhoneNumberUiEvent: VerifyPhoneNumberUiEvent = VerifyPhoneNumberUiEvent.Nothing,
        val dialogState: DialogUiState = DialogUiState.DialogInactive
    )

    sealed interface VerifyPhoneNumberUiEvent {
        object Nothing : VerifyPhoneNumberUiEvent
        object OnCodeSent : VerifyPhoneNumberUiEvent
        object OnVerificationComplete : VerifyPhoneNumberUiEvent
    }

    data class DeleteAccountState(
        val dialogState: DialogUiState = DialogUiState.DialogInactive,
        val isDeleteSuccess: Boolean = false
    )

    sealed interface DialogUiState {
        object DialogActive : DialogUiState
        object DialogInactive : DialogUiState
    }

    enum class DialogType {
        DELETE_ACCOUNT,
        VERIFY_PHONE_NUMBER,
        UPDATE_ACCOUNT_INFO,
        IMAGE_CROPPER
    }
}
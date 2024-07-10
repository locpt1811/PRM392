package com.example.finalproject.presentation.onboarding

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.utils.FIRST_TIME_LAUNCH
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Stable
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel(){
    fun setFinishOnboarding() {
        preferenceManager.saveData(FIRST_TIME_LAUNCH, false)
    }
}
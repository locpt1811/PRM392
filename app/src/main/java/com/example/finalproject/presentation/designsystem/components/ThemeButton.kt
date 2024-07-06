package com.example.finalproject.presentation.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.utils.MY_THEME
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    fun getThemePreference(): AppTheme {
        val themeString = preferenceManager.getData(MY_THEME, AppTheme.LIGHT.toString())
        return AppTheme.valueOf(themeString) // Convert String to AppTheme enum
    }

    fun saveThemePreference(theme: AppTheme) {
        preferenceManager.saveData(MY_THEME, theme.toString())
    }
}

@Composable
fun ThemeButton(viewModel: ThemeViewModel = hiltViewModel()) {
    var currentTheme by remember { mutableStateOf(AppTheme.LIGHT) }

    LaunchedEffect(key1 = Unit) {
        currentTheme = viewModel.getThemePreference()
    }

    Button(
        onClick = {
            currentTheme = when (currentTheme) {
                AppTheme.LIGHT -> {
                    viewModel.saveThemePreference(AppTheme.DARK)
                    AppTheme.DARK
                }
                AppTheme.DARK -> {
                    viewModel.saveThemePreference(AppTheme.LIGHT)
                    AppTheme.LIGHT
                }
            }
        }
    ) {
        Text("Change Theme")
    }
}

enum class AppTheme {
    LIGHT,
    DARK
}

@Preview(showBackground = true)
@Composable
fun ThemeButtonPreview() {
    MaterialTheme {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ThemeButton()
            }
        }
    }
}
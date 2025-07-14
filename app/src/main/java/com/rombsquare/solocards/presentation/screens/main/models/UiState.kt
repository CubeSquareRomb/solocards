package com.rombsquare.solocards.presentation.screens.main.models

import androidx.compose.material3.DrawerValue
import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.models.User
import kotlinx.coroutines.flow.MutableSharedFlow

data class UiState(
    val chosenFile: File? = null,
    val files: List<File> = emptyList(),
    val drawerValue: DrawerValue = DrawerValue.Closed,
    val tags: List<String> = emptyList(),
    val selectedTag: String = "all",
    val showAnswerParam: Boolean = false,
    val dialog: Dialog? = null,
    val action: UiAction? = null,
    val currentUser: User? = null,
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val currentToast: MutableSharedFlow<String> = MutableSharedFlow(),
    val lastDatabaseActionTime: Long = 0
)

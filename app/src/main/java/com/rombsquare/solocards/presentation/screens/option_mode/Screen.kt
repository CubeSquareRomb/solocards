package com.rombsquare.solocards.presentation.screens.option_mode

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.presentation.commons.components.EndGameDialog
import com.rombsquare.solocards.presentation.screens.option_mode.components.Options
import com.rombsquare.solocards.presentation.screens.option_mode.models.UiEvent
import com.rombsquare.solocards.presentation.screens.option_mode.models.UiState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OptionScreen(onEvent: (UiEvent?) -> Unit, uiState: State<UiState>, navigate: (String) -> Unit) {
    val context = LocalContext.current
    val uiState = uiState.value
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            uiState.currentToast.collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (uiState.showEndGameDialog) {
        EndGameDialog(
            score = uiState.correct,
            taskCount = uiState.cards.size,
            onHome = { navigate("main") }
        )
    }

    if (uiState.card == null) {
        return
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                Modifier.padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Task #${uiState.lvl+1}",
                        fontSize = 24.sp,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        uiState.card!!.question,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Options(
                    options = uiState.options,
                    correctIndex = uiState.correctIndex,
                    onOptionClick = { result ->
                        onEvent(UiEvent.ChooseOption(result))
                    }
                )
            }

        }
    )

}
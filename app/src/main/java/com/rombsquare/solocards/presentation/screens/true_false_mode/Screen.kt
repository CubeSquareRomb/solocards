package com.rombsquare.solocards.presentation.screens.true_false_mode

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.presentation.commons.components.EndGameDialog
import com.rombsquare.solocards.presentation.screens.true_false_mode.components.YesNoButtons
import com.rombsquare.solocards.presentation.screens.true_false_mode.models.UiEvent
import com.rombsquare.solocards.presentation.screens.true_false_mode.models.UiState
import kotlinx.coroutines.launch

@Composable
fun YesNoScreen(onEvent: (UiEvent?) -> Unit, uiState: State<UiState>, navigate: (String) -> Unit) {
    val context = LocalContext.current
    val uiState = uiState.value
    val scope = rememberCoroutineScope()

    if (uiState.card2 == null) {
        return
    }

    LaunchedEffect(Unit) {
        scope.launch {
            uiState.currentToast.collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (uiState.showEndGameDialog) {
        EndGameDialog(
            score = uiState.score,
            taskCount = uiState.cards.size,
            onHome = { navigate("main") }
        )
    }

    Scaffold(
        content = { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            uiState.cards.getOrNull(uiState.lvl)?.question ?: "",
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "Could the answer be:\n${uiState.card2.answer}?",
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                YesNoButtons(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    onEvent(UiEvent.ChooseOption(it))
                }
            }

        }
    )
}

package com.rombsquare.solocards

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rombsquare.solocards.di.AppEntryPoint
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.files.FileUseCases
import com.rombsquare.solocards.domain.usecases.prefs.PrefUseCases
import com.rombsquare.solocards.presentation.screens.editor.EditorScreen
import com.rombsquare.solocards.presentation.screens.main.MainMenuScreen
import com.rombsquare.solocards.presentation.screens.option_mode.OptionScreen
import com.rombsquare.solocards.presentation.screens.training_mode.TrainingGameScreen
import com.rombsquare.solocards.presentation.screens.true_false_mode.YesNoScreen
import com.rombsquare.solocards.presentation.screens.writing_mode.WritingGameScreen
import com.rombsquare.solocards.presentation.theme.SolocardsTheme
import com.rombsquare.solocards.presentation.screens.editor.EditorVM
import com.rombsquare.solocards.presentation.screens.main.GoogleAuthUiClient
import com.rombsquare.solocards.presentation.screens.main.MainMenuVM
import com.rombsquare.solocards.presentation.screens.option_mode.OptionGameVM
import com.rombsquare.solocards.presentation.screens.training_mode.TrainingGameVM
import com.rombsquare.solocards.presentation.screens.true_false_mode.YesNoGameVM
import com.rombsquare.solocards.presentation.screens.writing_mode.WritingGameVM
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.jvm.java
import com.google.android.gms.auth.api.identity.Identity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.rombsquare.solocards.domain.usecases.firestore.FirestoreUseCases
import com.rombsquare.solocards.presentation.screens.main.models.UiEvent
import javax.inject.Inject

val cards = listOf<List<String>>(
    listOf("What's the color of the cherry?", "Red"),
    listOf("What's the color of the lava?", "Orange"),
    listOf("What's the color of the sand?", "Yellow"),
    listOf("What's the color of the grass?", "Green"),
    listOf("What's the color of the sky?", "Cyan"),
    listOf("What's the color of the sea?", "Blue"),
    listOf("What's the rarest color for the flags?", "Purple"),
    listOf("What's the color of rose?", "Pink"),
    listOf("What's the color of black pants?", "Black"),
    listOf("What's the color of sadness", "Gray"),
    listOf("What's the color of the paper?", "White"),
    listOf("What's the color of the ground?", "Brown")
)

@HiltAndroidApp
class App : Application() {
    lateinit var prefUseCases: PrefUseCases
    lateinit var fileUseCases: FileUseCases
    lateinit var cardUseCases: CardUseCases

    override fun onCreate() {
        super.onCreate()

        val entryPoint: AppEntryPoint  = EntryPointAccessors.fromApplication(this, AppEntryPoint::class.java)

        prefUseCases = entryPoint.prefUseCases()

        if (!prefUseCases.getFirstRun()) {
            return
        }

        fileUseCases = entryPoint.fileUseCases()
        cardUseCases = entryPoint.cardUseCases()

        CoroutineScope(Dispatchers.IO).launch {
            val fileId = fileUseCases.addFile(File(
                name = "Example Quiz"
            )).toInt()

            cards.forEach { card ->
                cardUseCases.addCard(Card(
                    question = card[0],
                    answer = card[1],
                    fileId = fileId,
                    fixedOptions = card.size > 2,
                    option1 = card.getOrNull(2) ?: "",
                    option2 = card.getOrNull(3) ?: "",
                    option3 = card.getOrNull(4) ?: ""
                ))
            }
        }

        prefUseCases.setFirstRunFalse()
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firestoreUseCases: FirestoreUseCases

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BackHandler(enabled = true) {}

            SolocardsTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = false
                    val backgroundColor = MaterialTheme.colorScheme.background

                    // System bar always black
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = backgroundColor,
                            darkIcons = useDarkIcons
                        )
                    }

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "main") {

                        // Main Screen
                        composable("main") {
                            val viewModel: MainMenuVM = hiltViewModel()
                            val uiState = viewModel.uiState.collectAsState()

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if(result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            val user = googleAuthUiClient.getSignedInUser()

                                            if (user != null) {
                                                if (signInResult.isNewUser == true) {
                                                    firestoreUseCases.createDefaultUserData(user.userId)
                                                }

                                                viewModel.onEvent(UiEvent.SignIn(user))
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Signed in successful",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            )

                            MainMenuScreen(
                                onEvent = viewModel::onEvent,
                                uiState = uiState,
                                moveToEditor = { navController.navigate("editor/$it") },
                                onSignIn = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Wait please",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        viewModel.onEvent(UiEvent.SignOut)
                                        navController.popBackStack()
                                    }
                                },
                                user = googleAuthUiClient.getSignedInUser()
                            )
                        }

                        // Editor
                        composable(
                            "editor/{fileId}",
                            arguments = listOf(navArgument("fileId") { type = NavType.IntType } )
                        ) { backStackEntry ->
                            val viewModel: EditorVM = hiltViewModel()
                            val uiState = viewModel.uiState.collectAsState()
                            val currentDialog = viewModel.currentDialog.collectAsState()

                            EditorScreen(
                                onEvent = viewModel::onEvent,
                                uiState = uiState,
                                currentDialog = currentDialog,
                                moveToMainMenu = { navController.navigate("main") },
                                navigate = navController::navigate
                            )
                        }

                        // Option game
                        composable(
                            "option-game/{fileId}/{taskCount}",
                            arguments = listOf(
                                navArgument("fileId") { type = NavType.IntType },
                                navArgument("taskCount") {type = NavType.IntType}
                            )
                        ) {
                            val viewModel: OptionGameVM = hiltViewModel()
                            val uiState = viewModel.uiState.collectAsState()

                            OptionScreen(
                                onEvent = viewModel::onEvent,
                                uiState = uiState,
                                navigate = navController::navigate
                            )
                        }

                        // YesNo Game
                        composable(
                            "yes-no-game/{fileId}/{taskCount}",
                            arguments = listOf(
                                navArgument("fileId") { type = NavType.IntType },
                                navArgument("taskCount") {type = NavType.IntType}
                            )
                        ) {
                            val viewModel: YesNoGameVM = hiltViewModel()
                            val uiState = viewModel.uiState.collectAsState()

                            YesNoScreen(
                                onEvent = viewModel::onEvent,
                                uiState = uiState,
                                navigate = navController::navigate
                            )
                        }

                        // Writing game
                        composable(
                            "writing-game/{fileId}/{taskCount}",
                            arguments = listOf(
                                navArgument("fileId") { type = NavType.IntType },
                                navArgument("taskCount") {type = NavType.IntType}
                            )
                        ) {
                            val viewModel: WritingGameVM = hiltViewModel()
                            val uiState = viewModel.uiState.collectAsState()

                            WritingGameScreen(
                                onEvent = viewModel::onEvent,
                                uiState = uiState,
                                navigate = navController::navigate
                            )
                        }

                        // Training game
                        composable(
                            "training-game/{fileId}/{taskCount}",
                            arguments = listOf(
                                navArgument("fileId") { type = NavType.IntType },
                                navArgument("taskCount") {type = NavType.IntType}
                            )
                        ) {
                            val viewModel: TrainingGameVM = hiltViewModel()
                            val uiState = viewModel.uiState.collectAsState()

                            TrainingGameScreen(
                                onEvent = viewModel::onEvent,
                                uiState = uiState,
                                navigate = navController::navigate
                            )
                        }
                    }
                }
            }
        }
    }
}
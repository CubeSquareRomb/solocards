package com.rombsquare.solocards.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rombsquare.quiz.db.QuizDatabase
import com.rombsquare.solocards.data.firebase.data.FirestoreRepoImpl
import com.rombsquare.solocards.data.openai.AiRepoImpl
import com.rombsquare.solocards.data.room.cards.CardRepoImpl
import com.rombsquare.solocards.data.room.files.FileRepoImpl
import com.rombsquare.solocards.data.shared_prefs.PrefManager
import com.rombsquare.solocards.data.shared_prefs.PrefRepoImpl
import com.rombsquare.solocards.domain.repos.AiRepo
import com.rombsquare.solocards.domain.repos.CardRepo
import com.rombsquare.solocards.domain.repos.FileRepo
import com.rombsquare.solocards.domain.repos.FirestoreRepo
import com.rombsquare.solocards.domain.repos.PrefRepo
import com.rombsquare.solocards.domain.usecases.ai.AiUseCases
import com.rombsquare.solocards.domain.usecases.ai.GenerateCards
import com.rombsquare.solocards.domain.usecases.cards.AddCard
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.cards.DeleteAllCards
import com.rombsquare.solocards.domain.usecases.cards.DeleteCard
import com.rombsquare.solocards.domain.usecases.cards.GetCardsByFileId
import com.rombsquare.solocards.domain.usecases.cards.UpdateCard
import com.rombsquare.solocards.domain.usecases.files.AddFile
import com.rombsquare.solocards.domain.usecases.files.ClearTrash
import com.rombsquare.solocards.domain.usecases.files.DeleteAllFiles
import com.rombsquare.solocards.domain.usecases.files.DeleteFile
import com.rombsquare.solocards.domain.usecases.files.FileUseCases
import com.rombsquare.solocards.domain.usecases.files.GetFileById
import com.rombsquare.solocards.domain.usecases.files.GetFiles
import com.rombsquare.solocards.domain.usecases.files.GetFilesByTag
import com.rombsquare.solocards.domain.usecases.files.GetTags
import com.rombsquare.solocards.domain.usecases.files.RestoreFile
import com.rombsquare.solocards.domain.usecases.files.TrashFile
import com.rombsquare.solocards.domain.usecases.files.UpdateFile
import com.rombsquare.solocards.domain.usecases.firestore.AreEnoughTokens
import com.rombsquare.solocards.domain.usecases.firestore.CreateDefaultUserData
import com.rombsquare.solocards.domain.usecases.firestore.FirestoreUseCases
import com.rombsquare.solocards.domain.usecases.firestore.LoadData
import com.rombsquare.solocards.domain.usecases.firestore.RefillTokensIfPossible
import com.rombsquare.solocards.domain.usecases.firestore.SaveData
import com.rombsquare.solocards.domain.usecases.firestore.SubtractAiTokens
import com.rombsquare.solocards.domain.usecases.game.GameUseCases
import com.rombsquare.solocards.domain.usecases.game.GenerateOptions
import com.rombsquare.solocards.domain.usecases.game.GenerateOptionsByCards
import com.rombsquare.solocards.domain.usecases.prefs.GetFirstRun
import com.rombsquare.solocards.domain.usecases.prefs.GetShowAnswer
import com.rombsquare.solocards.domain.usecases.prefs.PrefUseCases
import com.rombsquare.solocards.domain.usecases.prefs.SetFirstRunFalse
import com.rombsquare.solocards.domain.usecases.prefs.SetShowAnswer
import com.rombsquare.solocards.domain.usecases.validators.ValidateCard
import com.rombsquare.solocards.domain.usecases.validators.ValidateName
import com.rombsquare.solocards.domain.usecases.validators.ValidateTags
import com.rombsquare.solocards.domain.usecases.validators.ValidatorUseCases
import com.rombsquare.solocards.presentation.screens.main.GoogleAuthUiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun prefUseCases(): PrefUseCases
    fun fileUseCases(): FileUseCases
    fun cardUseCases(): CardUseCases
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context,
        oneTapClient: com.google.android.gms.auth.api.identity.SignInClient
    ): GoogleAuthUiClient {
        return GoogleAuthUiClient(context, oneTapClient)
    }

    @Provides
    @Singleton
    fun provideOneTapClient(@ApplicationContext context: Context): com.google.android.gms.auth.api.identity.SignInClient {
        return com.google.android.gms.auth.api.identity.Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthUI(): AuthUI = AuthUI.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(app: Application): QuizDatabase {
        return Room.databaseBuilder(
            app,
            QuizDatabase::class.java,
            "quiz_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAiRepo(): AiRepo {
        return AiRepoImpl()
    }

    @Provides
    @Singleton
    fun provideFileRepo(db: QuizDatabase): FileRepo {
        return FileRepoImpl(db.fileDao())
    }

    @Provides
    @Singleton
    fun provideCardRepo(db: QuizDatabase): CardRepo {
        return CardRepoImpl(db.cardDao())
    }

    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext context: Context): PrefManager {
        return PrefManager(context)
    }

    @Provides
    @Singleton
    fun providePrefRepo(prefManager: PrefManager): PrefRepo {
        return PrefRepoImpl(prefManager)
    }

    @Provides
    @Singleton
    fun provideFirestoreRepo(firestore: FirebaseFirestore, fileRepo: FileRepo, cardRepo: CardRepo): FirestoreRepo {
        return FirestoreRepoImpl(firestore, fileRepo, cardRepo)
    }

    @Provides
    @Singleton
    fun provideFileUseCases(repo: FileRepo): FileUseCases {
        return FileUseCases(
            getFiles = GetFiles(repo),
            addFile = AddFile(repo),
            getTags = GetTags(repo),
            getFilesByTag = GetFilesByTag(repo),
            clearTrash = ClearTrash(repo),
            restoreFile = RestoreFile(repo),
            getFileById = GetFileById(repo),
            updateFile = UpdateFile(repo),
            deleteFile = DeleteFile(repo),
            trashFile = TrashFile(repo),
            deleteAllFiles = DeleteAllFiles(repo)
        )
    }
    @Provides
    @Singleton
    fun provideFirestoreUseCases(repo: FirestoreRepo): FirestoreUseCases {
        return FirestoreUseCases(
            saveData = SaveData(repo),
            loadData = LoadData(repo),
            createDefaultUserData = CreateDefaultUserData(repo),
            subtractAiTokens = SubtractAiTokens(repo),
            areEnoughTokens = AreEnoughTokens(repo),
            refillTokensIfPossible = RefillTokensIfPossible(repo),
        )
    }

    @Provides
    @Singleton
    fun provideAiUseCases(aiRepo: AiRepo, fileRepo: FileRepo, cardRepo: CardRepo): AiUseCases {
        return AiUseCases(
            generateCards = GenerateCards(aiRepo, fileRepo, cardRepo)
        )
    }

    @Provides
    @Singleton
    fun provideValidatorUseCases(): ValidatorUseCases {
        return ValidatorUseCases(
            validateCard = ValidateCard(),
            validateTags = ValidateTags(),
            validateName = ValidateName()
        )
    }

    @Provides
    @Singleton
    fun provideGameUseCases(): GameUseCases {
        return GameUseCases(
            generateOptions = GenerateOptions(),
            generateOptionsByCards = GenerateOptionsByCards()
        )
    }

    @Provides
    @Singleton
    fun provideCardUseCases(repo: CardRepo): CardUseCases {
        return CardUseCases(
            addCard = AddCard(repo),
            deleteAllCards = DeleteAllCards(repo),
            deleteCard = DeleteCard(repo),
            updateCard = UpdateCard(repo),
            getCardsByFileId = GetCardsByFileId(repo)
        )
    }

    @Provides
    @Singleton
    fun providePrefUseCases(repo: PrefRepo): PrefUseCases {
        return PrefUseCases(
            getShowAnswer = GetShowAnswer(repo),
            setShowAnswer = SetShowAnswer(repo),
            getFirstRun = GetFirstRun(repo),
            setFirstRunFalse = SetFirstRunFalse(repo)
        )
    }
}
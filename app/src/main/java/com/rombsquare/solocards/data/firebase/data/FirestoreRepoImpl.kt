package com.rombsquare.solocards.data.firebase.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.repos.CardRepo
import com.rombsquare.solocards.domain.repos.FileRepo
import com.rombsquare.solocards.domain.repos.FirestoreRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class FirestoreRepoImpl(
    private val firestore: FirebaseFirestore,
    private val fileRepo: FileRepo,
    private val cardRepo: CardRepo,
): FirestoreRepo {
    override suspend fun saveData(uid: String): Boolean {
        val cards = cardRepo.getAll()
        val files = fileRepo.getAll()

        val cardRef = firestore.collection("users")
            .document(uid)
            .collection("cards")

        val fileRef = firestore.collection("users")
            .document(uid)
            .collection("quizzes")

        try {
            clearCollection(cardRef)
            clearCollection(fileRef)


            cards.forEach { card ->
                firestore.collection("users")
                    .document(uid)
                    .collection("cards")
                    .add(card.toHashMap())
                    .addOnSuccessListener { documentReference ->
                    }
                    .addOnFailureListener { e ->
                    }
            }

            files.first().forEach { file ->
                firestore.collection("users")
                    .document(uid)
                    .collection("quizzes")
                    .add(file.toHashMap())
                    .addOnSuccessListener {}
                    .addOnFailureListener {}
            }

            return true
        } catch(e: Exception) {
            return false
        }
    }

    override suspend fun loadData(uid: String): Boolean {
        try {
            val quizzesSnapshot = firestore.collection("users")
                .document(uid)
                .collection("quizzes")
                .get()
                .await()

            val quizzes = quizzesSnapshot.documents.mapNotNull { doc ->
                doc.data?.let { data ->
                    File(
                        name = data["name"] as? String ?: "",
                        tags = data["tags"] as? List<String> ?: emptyList(),
                        isFav = data["isFav"] as? Boolean == true,
                        isTrashed = data["isTrashed"] as? Boolean == true,
                        id = (data["id"] as? Long)?.toInt() ?: (data["id"] as? Int) ?: -1
                    )
                }
            }

            val cardsSnapshot = firestore.collection("users")
                .document(uid)
                .collection("cards")
                .get()
                .await()

            val cards = cardsSnapshot.documents.mapNotNull { doc ->
                doc.data?.let { data ->
                    Card(
                        question = data["question"] as? String ?: "",
                        answer = data["answer"] as? String ?: "",
                        fileId = (data["fileId"] as? Long)?.toInt() ?: (data["fileId"] as? Int)
                        ?: -1,
                        fixedOptions = data["fixedOptions"] as? Boolean == true,
                        option1 = data["option1"] as? String ?: "",
                        option2 = data["option2"] as? String ?: "",
                        option3 = data["option3"] as? String ?: "",
                        id = (data["id"] as? Long)?.toInt() ?: (data["id"] as? Int) ?: -1
                    )
                }
            }

            if (cards.isEmpty() || quizzes.isEmpty()) {
                return false
            }

            fileRepo.deleteAll()
            cardRepo.deleteAll()

            quizzes.forEach { quiz ->
                fileRepo.insert(quiz)
            }

            cards.forEach { card ->
                cardRepo.insert(card)
            }

            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun createDefaultUserData(uid: String) {
        val defaults = hashMapOf(
            "aiTokens" to 100,
            "lastTokenRefill" to FieldValue.serverTimestamp()
        )

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("AI")
            .document("info")
            .set(defaults)
    }

    override suspend fun subtractAiTokens(uid: String, tokens: Int) {
        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("AI")
            .document("info")
            .update("aiTokens", FieldValue.increment(-tokens.toLong()))
            .addOnSuccessListener {}
            .addOnFailureListener {}
    }

    override suspend fun areEnoughTokens(uid: String, tokensToUse: Int): Boolean {
        val snapshot = Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("AI")
            .document("info")
            .get()
            .await()

        val aiTokens = snapshot.getLong("aiTokens")?.toInt() ?: 0

        return aiTokens >= tokensToUse
    }

    override suspend fun refillTokensIfPossible(uid: String) {
        val currentTimeMillis = System.currentTimeMillis()

        val snapshot = Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("AI")
            .document("info")
            .get()
            .await()

        val lastRefillTimestamp = snapshot.getTimestamp("lastTokenRefill")
        val lastRefillMillis = lastRefillTimestamp?.toDate()?.time ?: 0L

        if (currentTimeMillis - lastRefillMillis > 86_400_000) {
            Firebase.firestore
                .collection("users")
                .document(uid)
                .collection("AI")
                .document("info")
                .update(
                    mapOf(
                        "aiTokens" to 100,
                        "lastTokenRefill" to FieldValue.serverTimestamp()
                    )
                )
        }
    }
}

fun Card.toHashMap(): HashMap<String, Any?> {
    return hashMapOf(
        "id" to id,
        "question" to question,
        "answer" to answer,
        "fileId" to fileId,
        "fixedOptions" to fixedOptions,
        "option1" to option1,
        "option2" to option2,
        "option3" to option3,
    )
}

fun File.toHashMap(): HashMap<String, Any?> {
    return hashMapOf(
        "id" to id,
        "name" to name,
        "tags" to tags,
        "isFav" to isFav,
        "isTrashed" to isTrashed
    )
}

suspend fun clearCollection(collectionRef: CollectionReference) {
    val db = FirebaseFirestore.getInstance()
    val batchSize = 500L

    while (true) {
        val snapshot = collectionRef.limit(batchSize).get().await()
        if (snapshot.isEmpty) break

        val batch = db.batch()
        for (doc in snapshot.documents) {
            batch.delete(doc.reference)
        }
        batch.commit().await()
    }
}
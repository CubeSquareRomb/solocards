package com.rombsquare.solocards.data.openai

import com.rombsquare.solocards.BuildConfig
import com.rombsquare.solocards.domain.repos.AiRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AiRepoImpl: AiRepo {
    override suspend fun callOpenAI(description: String): String? {
        val client = OkHttpClient()
        val prompt = "You are helpful AI. Generate 20 flashcards using following pattern: \'<QUESTION> | <OPTION1> | <OPTION2> | <OPTION3> | <OPTION4> \', where OPTION1 is an answer (1 flashcard in 1 line). Flashcard must have very short answer. Questions must be clear and very short. No numeration needed, only raw text. Description of quiz: $description"
        val requestBody = """
        {
          "model": "gpt-3.5-turbo",
          "messages": [
            {"role": "user", "content": "$prompt"}
          ],
          "max_tokens": 500
        }
        """.trimIndent()

        val apiKey = BuildConfig.OPENAI_API_KEY

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaType()))
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext null
                }

                val responseBody = response.body?.string() ?: return@withContext null

                val json = JSONObject(responseBody)
                val choices = json.getJSONArray("choices")
                if (choices.length() == 0) return@withContext null

                val message = choices.getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim()

                return@withContext message
            }
        }
    }
}
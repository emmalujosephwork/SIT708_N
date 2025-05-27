package com.example.learningapptwo

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("generate_questions")
    suspend fun generateQuestions(@Body request: QuestionRequest): List<QuestionResponse>

    @POST("evaluate_answers")
    suspend fun evaluateAnswers(@Body request: EvaluationRequest): EvaluationResponse
}

data class QuestionRequest(
    val userId: String,
    val interests: List<String>,
    val difficulty: String = "medium"
)

data class QuestionResponse(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class EvaluationRequest(
    val userId: String,
    val answers: Map<String, String> // questionId to answer
)

data class EvaluationResponse(
    val score: Int,
    val explanations: Map<String, String>, // questionId to explanation
    val recommendations: List<String>
)
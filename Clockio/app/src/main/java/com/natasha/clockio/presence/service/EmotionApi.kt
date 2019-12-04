package com.natasha.clockio.presence.service

import com.natasha.clockio.presence.service.response.EmotionRequest
import com.natasha.clockio.presence.service.response.EmotionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface EmotionApi {
    @Headers(
        "Content-Type: application/json",
        "Ocp-Apim-Subscription-Key: 0491cab625b1413abb952f9487f0e30d"
    )
    @POST("/face/v1.0/detect")
    suspend fun detectFace(
        @Query("returnFaceAttributes") returnFaceAttributes: String,
        @Body emotionRequest: EmotionRequest)
            : Response<List<EmotionResponse>>
}
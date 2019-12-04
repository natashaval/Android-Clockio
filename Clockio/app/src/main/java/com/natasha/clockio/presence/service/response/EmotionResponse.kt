package com.natasha.clockio.presence.service.response

import java.io.Serializable

data class EmotionResponse (
    val faceId: String?,
    val faceRectangle: FaceRectangle?,
    val faceAttributes: FaceAttribute?
) : Serializable

data class FaceRectangle(
    val top: Int,
    val left: Int,
    val width: Int,
    val height: Int
)

data class FaceAttribute(
    val gender: String,
    val age: Double,
    val emotion: Emotion?
)

data class Emotion(
    val anger: Double?,
    val contempt: Double?,
    val disgust: Double?,
    val fear: Double?,
    val happiness: Double?,
    val neutral: Double?,
    val sadness: Double?,
    val surprise: Double?
)

data class EmotionRequest(
    val url: String
)
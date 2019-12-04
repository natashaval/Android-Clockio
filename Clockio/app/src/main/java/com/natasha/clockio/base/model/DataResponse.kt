package com.natasha.clockio.base.model

import java.io.Serializable

data class DataResponse (
    val success: Boolean,
    val message: String?,
    val data: Any?
) : Serializable
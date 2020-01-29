package com.natasha.clockio.base.model

data class PageResponse<T> (
    val content: List<T>?,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val numberOfElements: Int,
    val size: Int, // page size
    val number: Int // page number

)
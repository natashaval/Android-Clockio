package com.natasha.clockio.base.model

data class PageResponse<T> (
    val content: List<T>?,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
    val totalPages: Long,
    val totalElements: Long,
    val numberOfElements: Long,
    val size: Long, // page size
    val number: Long // page number

)
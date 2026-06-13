package com.dmm.recetario.domain.model

data class PageResponse<T> (
    val size: Int,
    val number: Int,
    val totalPages: Int,
    val content: List<T>,
    val totalElements: Long
)
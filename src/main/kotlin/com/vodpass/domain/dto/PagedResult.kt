package com.vodpass.domain.dto

import org.springframework.data.domain.Page

data class PagedResult<T : Any>(
    val content: List<T>,
    val totalElements: Long,
    val page: Int,
    val size: Int,
    val totalPages: Int
) {
    companion object {
        fun <T : Any> from(page: Page<T>): PagedResult<T> = PagedResult(
            content = page.content,
            totalElements = page.totalElements,
            page = page.number,
            size = page.size,
            totalPages = page.totalPages
        )
    }
}

package net.flow9.androidmvvm.repository.model.response.base

data class BaseDataPageable<T>(
        val content: List<T>,
        val empty: Boolean,
        val first: Boolean,
        val last: Boolean,
        val number: Int,
        val numberOfElements: Int,
        val pageable: Pageable,
        val size: Int,
        val sort: Sort,
        val totalElements: Int,
        val totalPages: Int
)
package net.flow9.androidmvvm.repository.model.response.base

abstract class BaseResponsePageable<T> {
    lateinit var data: BaseDataPageable<T>
    lateinit var error: Any
    lateinit var message: Any
}

abstract class BaseResponse<T> {
    lateinit var data: List<T>
    lateinit var error: Any
    lateinit var message: Any
}
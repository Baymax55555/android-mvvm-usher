package net.flow9.androidmvvm.repository.model.response

import net.flow9.androidmvvm.repository.model.response.base.BaseResponse

class City (
    val id: Int,
    val name: String
)

class CityResponse : BaseResponse<City>()
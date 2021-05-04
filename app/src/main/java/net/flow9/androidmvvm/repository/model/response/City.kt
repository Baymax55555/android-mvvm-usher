package net.flow9.androidmvvm.repository.model.response

import net.flow9.androidmvvm.activity.base.BaseDiffItem
import net.flow9.androidmvvm.repository.model.response.base.BaseResponse

class City (
    val id: Int,
    val name: String
) : BaseDiffItem {
    override fun getDiffId() = "$id"
}

class CityResponse : BaseResponse<City>()
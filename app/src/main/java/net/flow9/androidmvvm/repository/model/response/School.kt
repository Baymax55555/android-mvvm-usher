package net.flow9.androidmvvm.repository.model.response

import net.flow9.androidmvvm.activity.base.BaseDiffItem
import net.flow9.androidmvvm.repository.model.response.base.BaseResponsePageable

class School (
    val address: String,
    val apiSeq: Int,
    val category: String,
    val createDate: String,
    val id: Int,
    val isDeleted: Boolean,
    val name: String,
    val regionID: Int,
    val type: String,
    val updateDate: String
) : BaseDiffItem {
    override fun equals(other: Any?): Boolean {
        return id == (other as School).id
    }

    override fun getId() = "$id"
}

class SchoolResponse : BaseResponsePageable<School>()